package me.ryandw11.ultrachat.listener;

import me.clip.placeholderapi.util.jsonmessage.JSONMessage;
import me.ryandw11.ultrachat.api.ChannelChatEvent;
import me.ryandw11.ultrachat.api.JSON;
import me.ryandw11.ultrachat.formatting.PlayerFormatting;
import org.apache.logging.log4j.core.jackson.Log4jJsonObjectMapper;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.clip.placeholderapi.PlaceholderAPI;
import me.ryandw11.ultrachat.UltraChat;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * ChannelChatListener without any kind of json involved.
 * @author Ryandw11
 *
 */
public class ChannelChatListener implements Listener {
	private UltraChat plugin;
	private JSON json = new JSON();
	public ChannelChatListener(){
		plugin = UltraChat.plugin;
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e){
		Player p = e.getPlayer();

		// Set recipients
		String channel = plugin.data.getString(p.getUniqueId() + ".channel");
		if(!plugin.channel.getBoolean(channel + ".always_appear")){
			e.getRecipients().removeAll(Bukkit.getOnlinePlayers());
			if(p.hasPermission("ultrachat.chat.color")){
				e.setMessage(ChatColor.translateAlternateColorCodes('&', e.getMessage()));
			}
			else {
				e.setMessage(ChatColor.stripColor(e.getMessage()));
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
		e.setCancelled(true);

		ChannelChatEvent chatEvent = new ChannelChatEvent(e.getPlayer(), e.getRecipients(), channel, chatFormat, e.getMessage());
		Bukkit.getServer().getPluginManager().callEvent(chatEvent);
	}

	@EventHandler
	public void onChannelChat(ChannelChatEvent e) {
		String chatMessage = formatMessage(e.getPlayer(), e.getMessageFormat(), e.getMessage());
		PlayerFormatting pf = new PlayerFormatting(e.getPlayer());
		if(plugin.JSON) {
			//noinspection unchecked
			List<String> lore = (List<String>)plugin.channel.getList(e.getChannelName() + ".JSON");
			StringBuilder loreText = new StringBuilder();
			for(String loreLine : lore) {
				loreText.append(PlaceholderAPI.setPlaceholders(e.getPlayer(), ChatColor.translateAlternateColorCodes('&', loreLine))).append("\n");
			}
			JSONMessage msg = JSONMessage.create(chatMessage);
			msg.tooltip(loreText.toString());
			for(Player recipient : e.getRecipients()) {
				msg.send(recipient);
			}
		}
		else for(Player recipient : e.getRecipients()) {
			recipient.sendRawMessage(chatMessage);
		}
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
		if(plugin.JSON) {
			//json.hoverMessage(plugin.channel.getString(channel + ".prefix") + plugin.channel.getString(channel + ".format").replace("%prefix%", prefix).replace("%suffix%", suffix).replace("%player%", p.getDisplayName()),  (ArrayList<String>) plugin.channel.get(channel + ".JSON"), event.getMessage(), color, p).toString()
		}
		return outgoingMsg;
	}
}
