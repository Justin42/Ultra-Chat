package me.ryandw11.ultrachat.listener;

import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.TownyUniverse;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.util.jsonmessage.JSONMessage;
import me.ryandw11.ultrachat.UltraChat;
import me.ryandw11.ultrachat.api.*;
import me.ryandw11.ultrachat.formatting.PlayerFormatting;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.List;

/**
 * ChannelChatListener without any kind of json involved.
 * @author Ryandw11
 *
 */
public class ChannelChatListener implements Listener {
    private UltraChat plugin;
    private UltraChatAPI api;
    private JSON json = new JSON();

    public ChannelChatListener() {
        plugin = UltraChat.plugin;
        api = plugin.getAPI();
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();

        // Set recipients
        String defaultChannel = plugin.data.getString("Default_Channel", "global");
        String channel = plugin.data.getString(p.getUniqueId() + ".channel", defaultChannel);
        String scope = plugin.channel.getString(channel + ".scope", "global");

        if (p.hasPermission("ultrachat.chat.color")) {
            e.setMessage(ChatColor.translateAlternateColorCodes('&', e.getMessage()));
        } else {
            e.setMessage(ChatColor.stripColor(e.getMessage()));
        }

        if (!plugin.channel.getBoolean(channel + ".always_appear", false)) {
            e.getRecipients().removeAll(Bukkit.getOnlinePlayers());

            String permission = plugin.channel.getString(channel + ".permission", "none");

            if(scope.equalsIgnoreCase("global")) {
                for (Player pl : Bukkit.getOnlinePlayers()) {
                    if (!permission.equalsIgnoreCase("none") && !pl.hasPermission(permission)) continue;
                    if (!plugin.data.getString(pl.getUniqueId() + ".channel").equals(channel)) continue;
                    e.getRecipients().add(pl);
                }
            }
        }
        // TODO Add configuration options for empty-channel-message and default-on-empty
        else if (e.getRecipients().size() <= 1 && !channel.equalsIgnoreCase(defaultChannel)) {
            p.sendRawMessage(String.format(ChatColor.RED + "There is nobody else in the channel '%s'", channel));
            if (!channel.equalsIgnoreCase(defaultChannel)) {
                plugin.data.set(p.getUniqueId() + ".channel", defaultChannel);
                plugin.saveFile();
                p.sendMessage(String.format(ChatColor.RED + "You have been moved to the default channel '%s'", defaultChannel));
            }
        }
        if(!e.isCancelled()) {
            e.setCancelled(true);
            String chatFormat = plugin.channel.getString(channel + ".prefix") + plugin.channel.getString(channel + ".format");
            ChannelChatEvent chatEvent = new ChannelChatEvent(e.getPlayer(), e.getRecipients(), channel, chatFormat, e.getMessage());
            chatEvent.setScope(scope);
            Bukkit.getServer().getPluginManager().callEvent(chatEvent);
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void onChannelChat(ChannelChatEvent e) {
        if(!e.isCancelled()) {
            if (plugin.JSON) {
                JSONMessage jsonMessage = buildJSONMessage(e);
                for (Player recipient : e.getRecipients()) {
                    jsonMessage.send(recipient);
                }
            } else for (Player recipient : e.getRecipients()) {
                String chatMessage = formatMessage(e.getPlayer(), e.getMessageFormat(), e.getMessage());
                recipient.sendRawMessage(chatMessage);
            }
        }
    }

    @EventHandler
    private void onCommandOnSend(ChannelChatEvent e) {
        if (!e.getRecipients().isEmpty()) {
            // Handle command-on-send if there are recipients.
            List<?> commands = plugin.channel.getList(e.getChannelName() + ".command-on-send", null);
            if (commands != null) {
                //noinspection unchecked
                for (String command : (List<String>) commands) {
                    command = PlaceholderAPI.setPlaceholders(e.getPlayer(), command);
                    if (!command.trim().isEmpty()) {
                        plugin.getLogger().info(String.format("[%s] Issuing command:\n'%s'\n	triggered by user `%s` command-on-send handler for channel `%s`",
                                plugin.getDescription().getName(), command, e.getPlayer().getName(), e.getChannelName()));
                        runSyncCmd(plugin.getServer().getConsoleSender(), command);
                    }
                }
            }
        }
    }

    private void runSyncCmd(CommandSender sender, String cmd) {
        Bukkit.getScheduler().callSyncMethod( plugin, () ->
                Bukkit.dispatchCommand( sender, cmd)
        );
    }

	private JSONMessage buildJSONMessage(ChannelChatEvent e) {
		String chatMessage = formatMessage(e.getPlayer(), e.getMessageFormat(), e.getMessage());
		PlayerFormatting pf = new PlayerFormatting(e.getPlayer());

		JSONMessage nameHover = Util.buildLore(plugin.channel.getList(e.getChannelName() + ".format-hover"), e.getPlayer());
		JSONMessage messageHover = Util.buildLore(plugin.channel.getList(e.getChannelName() + ".message-hover"), e.getPlayer());
		String nameSuggestCmd = PlaceholderAPI.setPlaceholders(e.getPlayer(), plugin.channel.getString(e.getChannelName() + ".format-click-suggest", ""));
		String messageSuggestCmd = PlaceholderAPI.setPlaceholders(e.getPlayer(), plugin.channel.getString(e.getChannelName() + ".message-click-suggest", ""));
		String message = e.getMessage();
		/*if(e.getPlayer().hasPermission("ultrachat.itemlink")) {
			message = message; // TODO
		}*/

		JSONMessage msg = JSONMessage.create(formatMessagePrepends(e.getPlayer(), e.getMessageFormat()));
		if(nameHover != null) msg.tooltip(nameHover);
		if(!nameSuggestCmd.isEmpty()) msg.suggestCommand(nameSuggestCmd);
		if(e.getPlayer().hasPermission("ultrachat.chat.color")) {
			message = ChatColor.translateAlternateColorCodes('&', message);
			msg.then(message).color(pf.getColor());
		}
		else {
			msg.then(message);
		}
		if(!messageSuggestCmd.isEmpty()) msg.suggestCommand(messageSuggestCmd);
		if(messageHover != null) msg.tooltip(messageHover);
		return msg;
	}

	private String formatMessage(Player p, String msgFormat, String msg) {
		PlayerFormatting pf = new PlayerFormatting(p);
		if (p.hasPermission("ultrachat.chat.color")) {
			msg = ChatColor.translateAlternateColorCodes('&', msg);
		}
		else msg = ChatColor.stripColor(msg);
		String outgoingMsg = msgFormat.replace("%player%", p.getDisplayName())
				.replace("%prefix%", pf.getPrefix())
				.replace("%suffix%", pf.getSuffix());
		outgoingMsg = PlaceholderAPI.setPlaceholders(p, outgoingMsg);
		outgoingMsg += pf.getColor() + msg;
		return outgoingMsg;
	}

	private String formatMessagePrepends(Player p, String msgFormat) {
		PlayerFormatting pf = new PlayerFormatting(p);
		String outgoingMsg = msgFormat.replace("%player%", p.getDisplayName())
				.replace("%prefix%", pf.getPrefix())
				.replace("%suffix%", pf.getSuffix());
		//outgoingMsg = ChatColor.translateAlternateColorCodes('&', outgoingMsg);
		outgoingMsg = PlaceholderAPI.setPlaceholders(p, outgoingMsg);
		outgoingMsg = ChatColor.translateAlternateColorCodes('&', outgoingMsg);
		System.out.println(outgoingMsg);
		return outgoingMsg;
	}
}
