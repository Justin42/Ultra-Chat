package me.ryandw11.ultrachat.listner;

import java.util.List;

import me.ryandw11.ultrachat.core.UltraChat;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
/*
 * ================================{UltraChat}================================
 * Developed by: Ryandw11
 * ================================{UltraChat}================================
 */
public class NoSwear implements Listener {
	
	private UltraChat plugin;
	public NoSwear(UltraChat plugin){
		this.plugin = plugin;
	}
	

	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		
		if(plugin.getConfig().getBoolean("Anti_Swear_Enabled")){	
			
			
			Player p = event.getPlayer();
		
		
		List <String> swear = plugin.getConfig().getStringList("Blocked_Words");
		int times = 0;
		
		String Message = " " + event.getMessage().toLowerCase().replace(".", "") + " ";
		
		for(String swearWord : swear){
			//Check if world chat is enabled
			if(Message.contains(swearWord + " ") && times == 0 || Message.contains(" " + swearWord + " ") && times == 0 || Message.contains(" " + swearWord) && times == 0 || Message.contains(swearWord)){
				//else do this:
				event.setCancelled(true);
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Swear_Message")));
				
			}
			
			else{}
		}
		}
	}//

}
