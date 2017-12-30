package me.ryandw11.ultrachat.listner;

import java.util.List;

import me.clip.placeholderapi.PlaceholderAPI;
import me.ryandw11.ultrachat.UltraChat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
/**
 * ================================{UltraChat}================================
 * Developed by: Ryandw11
 * ================================{UltraChat}================================
 */
public class JoinListner implements Listener {
	
	private UltraChat plugin;
	public JoinListner(UltraChat plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		
		
		
		Player p = event.getPlayer();
		
		if(plugin.data.contains(p.getUniqueId().toString())){
			if(!(plugin.channel.contains(plugin.data.getString(p.getUniqueId() + ".channel")))){
				/*
				 * Fail safe so that if the player joins with an invalid channel it goes back to default.
				 */
				plugin.data.set(p.getUniqueId() + ".channel", plugin.getConfig().getString("Default_Channel"));
				plugin.saveFile();
				
			}else if(!(plugin.data.contains(p.getUniqueId().toString() + ".spy"))){
				plugin.data.set(p.getUniqueId().toString() + ".spy", false);
				plugin.saveFile();
			}
		}
		else{
			plugin.data.set(p.getUniqueId().toString() + ".color", "&f");
			plugin.data.set(p.getUniqueId().toString() + ".sjoin", false);
			plugin.data.set(p.getUniqueId().toString() + ".spy", false);
			plugin.data.set(p.getUniqueId().toString() + ".channel", plugin.getConfig().getString("Default_Channel"));
			plugin.saveFile();	
		}
		
		if(plugin.data.getBoolean(p.getUniqueId().toString() + ".sjoin")){
			event.setJoinMessage("");
			for(Player pl : Bukkit.getOnlinePlayers()){
				if(pl.hasPermission("ultrachat.sjoin.alert")){
					pl.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Silent_Join_Message")).replace("%player%", p.getDisplayName()));
				}
			}
			
		}else{
		
		String Join = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Join_Message").replace("%player%", p.getName()));
		event.setJoinMessage(Join);
		}
		
		
	}
	
	
	
	
	
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event){
		
		Player p = event.getPlayer();
		if(plugin.data.getBoolean(p.getUniqueId().toString() + ".sjoin")){
			event.setQuitMessage("");
			for(Player pl : Bukkit.getOnlinePlayers()){
				if(pl.hasPermission("ultrachat.sjoin.alert")){
					pl.sendMessage(ChatColor.translateAlternateColorCodes('&',plugin.getConfig().getString("Silent_Leave_Message")).replace("%player%", p.getDisplayName()));
				}
			}
		}else{
		
		String leave = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Leave_Message").replace("%player%", p.getName()));
		event.setQuitMessage(leave);
		}
	}
	
	
	
	
	
	@EventHandler
	public void MOTD(PlayerJoinEvent event){
		Player p = event.getPlayer();
		if(plugin.getConfig().getBoolean("Motd_Enabled")){
			List <String> motd = plugin.getConfig().getStringList("Motd");
			for(String OutPut : motd){
				String message = OutPut;
				message = PlaceholderAPI.setPlaceholders(p, message);
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
			}

		}
		
	}
	
	@EventHandler
	public void NewPlayer(PlayerJoinEvent event){
		Player p = event.getPlayer();
		
		
		if(!(p.hasPlayedBefore()) && !(plugin.getConfig().getString("New_Player").equalsIgnoreCase("none"))){
			Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("New_Player").replace("%player%", p.getDisplayName())));
		}
		
	}
	
	
	
}
