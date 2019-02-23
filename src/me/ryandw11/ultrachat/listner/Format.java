package me.ryandw11.ultrachat.listner;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.clip.placeholderapi.PlaceholderAPI;
import me.ryandw11.ultrachat.UltraChat;
import me.ryandw11.ultrachat.api.JSON;
import me.ryandw11.ultrachat.api.JsonChatEvent;

import me.ryandw11.ultrachat.formatting.PlayerFormatting;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * @author Ryandw11
 * Kind of messsy for right now.
 */

public class Format implements Listener {
	
	private JSON json;
	private UltraChat plugin;
	public Format(UltraChat plugin){
		this.plugin = plugin;
		json = new JSON();
	}
	
	
	private String prefix;
	private String suffix;
	private ChatColor color;
	private String formatOp;
	private String defaults;
	
	
	@SuppressWarnings("unchecked")
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e){
		if(!plugin.getConfig().getBoolean("Custom_Chat_Enabled")) return;
		
		Player p = e.getPlayer();
		color = ChatColor.getByChar(ChatColor.translateAlternateColorCodes('&', plugin.data.getString(p.getUniqueId() + ".color")).replace("&", ""));
		
		prefix = ChatColor.translateAlternateColorCodes('&', plugin.chat.getPlayerPrefix(p));
		suffix = ChatColor.translateAlternateColorCodes('&', plugin.chat.getPlayerSuffix(p));

		PlayerFormatting pFormat = new PlayerFormatting(e.getPlayer());
		formatOp = PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Custom_Chat.Op_Chat.Format")));
		defaults = PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Custom_Chat.Default_Chat.Format")));
		
		if(plugin.channelEnabled){
			/*
			 * If json and channels are enabled
			 */
			if(plugin.JSON){
				e.setCancelled(true);
				String channel = plugin.data.getString(p.getUniqueId() + ".channel");
				if(!plugin.channel.getBoolean(channel + ".always_appear")){
					JsonChatEvent event = new JsonChatEvent(p, e.getMessage(), new HashSet<Player>());
					Bukkit.getServer().getPluginManager().callEvent(event);
					if(!event.isCancelled())
					for(Player pl : Bukkit.getOnlinePlayers()){
						if(plugin.data.getString(pl.getUniqueId() + ".channel").equals(channel)){
							if(pl.hasPermission(plugin.channel.getString(channel + ".permission")) || plugin.channel.getString(channel + ".permission").equalsIgnoreCase("none")){
								
									pl.sendRawMessage(json.hoverMessage(plugin.channel.getString(channel + ".prefix") + plugin.channel.getString(channel + ".format").replace("%prefix%", prefix).replace("%suffix%", suffix).replace("%player%", p.getDisplayName()),  (ArrayList<String>) plugin.channel.get(channel + ".JSON"), event.getMessage(), color, p).toString());
							}
						}

						}
					}
				else{
					JsonChatEvent event = new JsonChatEvent(p, e.getMessage(), new HashSet<Player>());
					Bukkit.getServer().getPluginManager().callEvent(event);
					if(!event.isCancelled())
						for(Player pl : Bukkit.getOnlinePlayers()){
							pl.sendRawMessage(json.hoverMessage(plugin.channel.getString(channel + ".prefix") + plugin.channel.getString(channel + ".format").replace("%prefix%", prefix).replace("%suffix%", suffix).replace("%player%", p.getDisplayName()),  (ArrayList<String>) plugin.channel.get(channel + ".JSON"), event.getMessage(), color, p).toString());
						}
				}
			}
			else{
				/*
				 * If channels are enabled and json is not.
				 */
				String channel = plugin.data.getString(p.getUniqueId() + ".channel");
				if(!plugin.channel.getBoolean(channel + ".always_appear")){
					e.getRecipients().removeAll(Bukkit.getOnlinePlayers());
					for(Player pl : Bukkit.getOnlinePlayers()){
						if(plugin.data.getString(pl.getUniqueId() + ".channel").equals(channel)){
							if(pl.hasPermission(plugin.channel.getString(channel + ".permission")) || plugin.channel.getString(channel + ".permission").equalsIgnoreCase("none")){
								e.getRecipients().add(pl);
							}
						}

						}
					}
					e.setFormat(ChatColor.translateAlternateColorCodes('&', plugin.channel.getString(channel + ".prefix")) + ChatColor.translateAlternateColorCodes('&', plugin.channel.getString(channel + ".format").replace("%prefix%", prefix).replace("%suffix%", suffix).replace("%player%", "%s") + color + "%s"));	
			}
		}
		/*
		 * Channels are not enabled
		 */
		else{
			/*
			 * JSON without channels
			 */
			if(plugin.JSON){
				boolean complete = false;
				e.setCancelled(true);
				if(p.isOp() && pFormat.getOpFormatEnabled()){
					JsonChatEvent event = new JsonChatEvent(p, e.getMessage(), new HashSet<Player>());
					Bukkit.getServer().getPluginManager().callEvent(event);
					if(!event.isCancelled())
						for(Player pl : Bukkit.getOnlinePlayers()){
							pl.sendRawMessage(json.hoverMessage(formatOp.replace("%prefix%", prefix).replace("%suffix%", suffix).replace("%player%", p.getDisplayName()), (ArrayList<String>) plugin.getConfig().get("Custom_Chat.Op_Chat.JSON"), event.getMessage(), color, p).toString());
						}
				}else{
					int i = 1;
					JsonChatEvent event = new JsonChatEvent(p, e.getMessage(), new HashSet<Player>());
					Bukkit.getServer().getPluginManager().callEvent(event);
					if(!event.isCancelled()){
						while(i <= plugin.getConfig().getInt("Custom_Chat.Chat_Count")){
							if(p.hasPermission(plugin.getConfig().getString("Custom_Chat." + i + ".Permission"))){
								for(Player pl : Bukkit.getOnlinePlayers()){
									pl.sendMessage(json.hoverMessage(plugin.getConfig().getString("Custom_Chat." + i +".Format").replace("%player%", p.getDisplayName()).replace("%prefix%", prefix).replace("%suffix%", suffix), (ArrayList<String>) plugin.getConfig().get("Custom_Chat." + i +".JSON"), event.getMessage(), color, p).toString());
									complete = true;
								}
							}
							i++; // Fixed
						}
					}
					/*
					 * Normal player check
					 */
					if(!complete){
						JsonChatEvent events = new JsonChatEvent(p, e.getMessage(), new HashSet<Player>());
						Bukkit.getServer().getPluginManager().callEvent(events);
						if(!event.isCancelled())
							for(Player pl : Bukkit.getOnlinePlayers()){ // Fixed for normal players
								pl.sendMessage(json.hoverMessage(defaults.replace("%prefix%", prefix).replace("%suffix%", suffix).replace("%player%", p.getDisplayName()), (ArrayList<String>) plugin.getConfig().get("Custom_Chat.Default_Chat.JSON"), event.getMessage(), color, p).toString());
							}
					}
				}
			}
			/*
			 * Normal chat with no JSON and channels.
			 */
			else{
				if(p.isOp() && pFormat.getOpFormatEnabled()){
					e.setFormat(formatOp.replace("%prefix%", prefix).replace("%suffix%", suffix).replace("%player%", "%s") + color + "%s");
				}else{
					int i = 1;
					e.setFormat(defaults.replace("%prefix%", prefix).replace("%suffix%", suffix).replace("%player%", "%s") + color + "%s");
					while(i <= plugin.getConfig().getInt("Custom_Chat.Chat_Count")){
						if(p.hasPermission(plugin.getConfig().getString("Custom_Chat." + i + ".Permission"))){
							e.setFormat(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Custom_Chat." + i +".Format").replace("%player%", "%s").replace("%prefix%", prefix).replace("%suffix%", suffix)) + color + "%s"));	
						}
						i++; //Fixed
					}	
				}
			}// end of json op
		}
	}//END
}