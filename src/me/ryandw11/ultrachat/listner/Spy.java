package me.ryandw11.ultrachat.listner;

import me.ryandw11.ultrachat.commands.spyCommand;
import me.ryandw11.ultrachat.core.UltraChat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class Spy implements Listener{
	
	private UltraChat plugin;
	public Spy(UltraChat plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerCommand(PlayerCommandPreprocessEvent event){
		Player p1 = (Player) event.getPlayer();
		String msg = event.getMessage();//get user name
		
			for(Player p : Bukkit.getOnlinePlayers()){
				if(spyCommand.spytoggle.contains(p.getUniqueId())){
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Command_Spy_Prefix")) + ChatColor.DARK_AQUA + p1.getName() + ": " + ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Command_Spy_Color")) + msg);                                  
				
				
				}// end of if
				
			
			}//end of for loop
				
				
			
		
	}
	
	
}