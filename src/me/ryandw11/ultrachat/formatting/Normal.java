package me.ryandw11.ultrachat.formatting;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.clip.placeholderapi.PlaceholderAPI;
import me.ryandw11.ultrachat.UltraChat;
/**
 * Normal chat formatting with no channels or Json.
 * @author Ryandw11
 *
 */
public class Normal implements Listener {
	private UltraChat plugin;
	public Normal(){
		plugin = UltraChat.plugin;
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e){
		PlayerFormatting pf = new PlayerFormatting(e.getPlayer());
		Player p = e.getPlayer();
		if(p.isOp()){
			e.setFormat(pf.getOpFormat().replace("%prefix%", pf.getPrefix()).replace("%suffix%", pf.getSuffix()).replace("%player%", "%s") + pf.getColor() + "%s");
		}else{
			int i = 1;
			e.setFormat(pf.getDefaultFormat().replace("%prefix%", pf.getPrefix()).replace("%suffix%", pf.getSuffix()).replace("%player%", "%s") + pf.getColor() + "%s");
			while(i <= plugin.getConfig().getInt("Custom_Chat.Chat_Count")){
				if(p.hasPermission(plugin.getConfig().getString("Custom_Chat." + i + ".Permission"))){
					e.setFormat(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Custom_Chat." + i +".Format").replace("%player%", "%s").replace("%prefix%", pf.getPrefix()).replace("%suffix%", pf.getSuffix())) + pf.getColor() + "%s"));	
				}
				i++;
			}	
		}
	}

}
