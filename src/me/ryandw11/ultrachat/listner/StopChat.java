package me.ryandw11.ultrachat.listner;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.ryandw11.ultrachat.UltraChat;

public class StopChat implements Listener {
	
	private UltraChat plugin;
	public StopChat(UltraChat plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		
		Player p = event.getPlayer();
		if(plugin.chatStop == true){
			if(!(p.hasPermission("ultrachat.stopchat.bypass"))){
				event.setCancelled(true);
			
			String Stop_Chat_Message = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Stop_Chat_Message"));
			
			p.sendMessage(Stop_Chat_Message);
		}
		}//============================================================================================
		
		
	}
	
	
	
}
