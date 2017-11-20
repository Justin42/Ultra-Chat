package me.ryandw11.ultrachat.listner;

import me.ryandw11.ultrachat.core.UltraChat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class Notify implements Listener {

	@SuppressWarnings("unused")
	private UltraChat plugin;
	public Notify(UltraChat plugin){
		this.plugin = plugin;
	}
	
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event){
		for(Player p : Bukkit.getOnlinePlayers()){
			if(event.getMessage().contains("@" + p.getName())){
				p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 10, 0);
				p.sendMessage(ChatColor.GREEN + "Someone has mentioned you!");
			}
		}
	}

	
}
