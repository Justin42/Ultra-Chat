package me.ryandw11.ultrachat.listener;

import me.ryandw11.ultrachat.api.ChannelChatEvent;
import me.ryandw11.ultrachat.formatting.PlayerFormatting;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.clip.placeholderapi.PlaceholderAPI;
import me.ryandw11.ultrachat.UltraChat;

/**
 * ChannelChatListener without any kind of json involved.
 * @author Ryandw11
 *
 */
public class ChannelChatListener implements Listener {
	private UltraChat plugin;
	public ChannelChatListener(){
		plugin = UltraChat.plugin;
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e){
		PlayerFormatting pf = new PlayerFormatting(e.getPlayer());
		Player p = e.getPlayer();

		// Set recipients
		String channel = plugin.data.getString(p.getUniqueId() + ".channel");
		if(!plugin.channel.getBoolean(channel + ".always_appear")){
			e.getRecipients().removeAll(Bukkit.getOnlinePlayers());
			if(p.hasPermission("ultrachat.chat.color")){
				e.setMessage(ChatColor.translateAlternateColorCodes('&', e.getMessage()));
			}
			for(Player pl : Bukkit.getOnlinePlayers()){
				if(plugin.data.getString(pl.getUniqueId() + ".channel").equals(channel)){
					if (pl.hasPermission(plugin.channel.getString(channel + ".permission"))) {
						e.getRecipients().add(pl);
					} else if (plugin.channel.getString(channel + ".permission").equalsIgnoreCase("none")) {
						e.getRecipients().add(pl);
					}
				}
			}
		}
		String chatFormat = plugin.channel.getString(channel + ".prefix") + plugin.channel.getString(channel + ".format");
		/*e.setFormat(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Custom_Chat." + i +".Format").replace("%player%", "%s").replace("%prefix%", pf.getPrefix()).replace("%suffix%", pf.getSuffix())) + pf.getColor() + "%s"));
		e.setFormat(PlaceholderAPI.setPlaceholders(p,
				ChatColor.translateAlternateColorCodes('&', plugin.channel.getString(channel + ".prefix"))
						+ ChatColor.translateAlternateColorCodes('&', plugin.channel.getString(channel + ".format")
						.replace("%prefix%", pf.getPrefix())
						.replace("%suffix%", pf.getSuffix())
						.replace("%player%", "%s")
						+ pf.getColor() + "%s")));*/
		e.setCancelled(true);
		ChannelChatEvent chatEvent = new ChannelChatEvent(e.getPlayer(), e.getRecipients(), channel, chatFormat, e.getMessage());
		Bukkit.getServer().getPluginManager().callEvent(chatEvent);
	}

	@EventHandler
	public void onChannelChat(ChannelChatEvent e) {
		String chatMessage = formatMessage(e.getPlayer(), e.getMessageFormat(), e.getMessage());
		for(Player recipient : e.getRecipients()) {
			recipient.sendRawMessage(chatMessage);
		}
	}

	private static String formatMessage(Player p, String msgFormat, String msg) {
		PlayerFormatting pf = new PlayerFormatting(p);
		if (p.hasPermission("ultrachat.chat.color")) {
			msg = ChatColor.translateAlternateColorCodes('&', msg);
		}
		String outgoingMsg = msgFormat.replace("%player%", p.getDisplayName())
				.replace("%prefix%", pf.getPrefix())
				.replace("%suffix%", pf.getSuffix());
		outgoingMsg = PlaceholderAPI.setPlaceholders(p, outgoingMsg);
		outgoingMsg += pf.getColor() + msg;
		return outgoingMsg;
	}
}
