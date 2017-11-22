package me.ryandw11.ultrachat.listner;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.ryandw11.ultrachat.UltraChat;
import me.ryandw11.ultrachat.api.JsonChatEvent;
/**
 * Prevent players from chatting when the chat is stopped.
 * @author Ryandw11
 *
 */
public class StopChat implements Listener {
	
	private UltraChat plugin;
	public StopChat(){
		plugin = UltraChat.plugin;
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {	
		Player p = event.getPlayer();
		if(plugin.chatStop){
			if(!p.hasPermission("ultrachat.stopchat.bypass")){
				event.setCancelled(true);
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Stop_Chat_Message")));
			}
		}
	}
	
	@EventHandler
	public void onChat(JsonChatEvent e){
		Player p = e.getPlayer();
		if(plugin.chatStop){
			if(!p.hasPermission("ultrachat.stopchat.bypass")){
				e.setCancelled(true);
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Stop_Chat_Message")));
			}
		}
	}
	
	
	
}
