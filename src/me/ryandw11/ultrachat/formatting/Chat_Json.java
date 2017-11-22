package me.ryandw11.ultrachat.formatting;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.ryandw11.ultrachat.UltraChat;
import me.ryandw11.ultrachat.api.JSON;
import me.ryandw11.ultrachat.api.JsonChatEvent;
/**
 * If JSON is enabled.
 * @author Ryandw11
 *
 */
public class Chat_Json implements Listener{
	private UltraChat plugin;
	private JSON json;
	public Chat_Json(){
		plugin = UltraChat.plugin;
		json = new JSON();
	}
	
	@SuppressWarnings("unchecked")
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e){
		Player p = e.getPlayer();
		PlayerFormatting pf = new PlayerFormatting(p);
		/*
		 * Check if Channel and JSON is enabled.
		 */
		if(plugin.channelEnabled){
			e.setCancelled(true);
			String channel = plugin.data.getString(p.getUniqueId() + ".channel");
			if(!plugin.channel.getBoolean(channel + ".always_appear")){
				JsonChatEvent event = new JsonChatEvent(p, e.getMessage());
				Bukkit.getServer().getPluginManager().callEvent(event);
				if(!event.isCancelled())
				for(Player pl : Bukkit.getOnlinePlayers()){
					if(plugin.data.getString(pl.getUniqueId() + ".channel").equals(channel)){
						if(pl.hasPermission(plugin.channel.getString(channel + ".permission")) || plugin.channel.getString(channel + ".permission").equalsIgnoreCase("none")){
							
								pl.spigot().sendMessage(json.hoverMessage(plugin.channel.getString(channel + ".prefix") + plugin.channel.getString(channel + ".format").replace("%prefix%", pf.getPrefix()).replace("%suffix%", pf.getSuffix()).replace("%player%", p.getDisplayName()),  (ArrayList<String>) plugin.channel.get(channel + ".JSON"), event.getMessage(), pf.getColor(), p));
						}
					}

					}
				}
			else{
				JsonChatEvent event = new JsonChatEvent(p, e.getMessage());
				Bukkit.getServer().getPluginManager().callEvent(event);
				if(!event.isCancelled())
					for(Player pl : Bukkit.getOnlinePlayers()){
						pl.spigot().sendMessage(json.hoverMessage(plugin.channel.getString(channel + ".prefix") + plugin.channel.getString(channel + ".format").replace("%prefix%", pf.getPrefix()).replace("%suffix%", pf.getPrefix()).replace("%player%", p.getDisplayName()),  (ArrayList<String>) plugin.channel.get(channel + ".JSON"), event.getMessage(), pf.getColor(), p));
					}
			}
		}else{ //if Channel is not enabled.
			boolean complete = false;
			e.setCancelled(true);
			if(p.isOp()){
				JsonChatEvent event = new JsonChatEvent(p, e.getMessage());
				Bukkit.getServer().getPluginManager().callEvent(event);
				if(!event.isCancelled())
					for(Player pl : Bukkit.getOnlinePlayers()){
						pl.spigot().sendMessage(json.hoverMessage(pf.getOpFormat().replace("%prefix%", pf.getPrefix()).replace("%suffix%", pf.getSuffix()).replace("%player%", p.getDisplayName()), (ArrayList<String>) plugin.getConfig().get("Custom_Chat.Op_Chat.JSON"), event.getMessage(), pf.getColor(), p));
					}
			}else{
				int i = 1;
				JsonChatEvent event = new JsonChatEvent(p, e.getMessage());
				Bukkit.getServer().getPluginManager().callEvent(event);
				if(!event.isCancelled()){
					while(i <= plugin.getConfig().getInt("Custom_Chat.Chat_Count")){
						if(p.hasPermission(plugin.getConfig().getString("Custom_Chat." + i + ".Permission"))){
							for(Player pl : Bukkit.getOnlinePlayers()){
								pl.spigot().sendMessage(json.hoverMessage(plugin.getConfig().getString("Custom_Chat." + i +".Format").replace("%player%", p.getDisplayName()).replace("%prefix%", pf.getPrefix()).replace("%suffix%", pf.getSuffix()), (ArrayList<String>) plugin.getConfig().get("Custom_Chat." + i +".JSON"), event.getMessage(), pf.getSuffix(), p));
								complete = true;
							}
						}
						i++;
					}
				}
				/*
				 * Normal player check
				 */
				if(!complete){
					JsonChatEvent events = new JsonChatEvent(p, e.getMessage());
					Bukkit.getServer().getPluginManager().callEvent(events);
					if(!event.isCancelled())
						for(Player pl : Bukkit.getOnlinePlayers()){ // Fixed for normal players
							pl.spigot().sendMessage(json.hoverMessage(pf.getDefaultFormat().replace("%prefix%", pf.getPrefix()).replace("%suffix%", pf.getSuffix()).replace("%player%", p.getDisplayName()), (ArrayList<String>) plugin.getConfig().get("Custom_Chat.Default_Chat.JSON"), event.getMessage(), pf.getColor(), p));
						}
				}
			}
		}
	}

}