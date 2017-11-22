package me.ryandw11.ultrachat.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ryandw11.ultrachat.UltraChat;

public class StaffChat implements CommandExecutor {
	private UltraChat plugin;
	public StaffChat(){
		plugin = UltraChat.plugin;
	}


	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
		
		CommandSender p = sender;
		if(p.hasPermission("ultrachat.staffchat")){
			String Message = "";
				
			if(args.length > 0){
				
				for (int i = 0; i < args.length; i++){
					Message = Message + " " + args[i];
					
				}
				for(Player p1 : Bukkit.getOnlinePlayers()){
					if(p1.hasPermission("ultrachat.staffchat")){
						if(!plugin.stafftoggle.contains(p1.getUniqueId())){
							p1.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Staff_Chat_Prefix")) + ChatColor.DARK_AQUA + p.getName() + ":" + ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Staff_Chat_Color")) + Message);
						}// end of if
					}
				}//end of for
				plugin.getLogger().info(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Staff_Chat_Prefix")) + ChatColor.DARK_AQUA + p.getName() + ":" + ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Staff_Chat_Color")) + Message);
			}//end
			else{
				p.sendMessage(ChatColor.RED + "Not enough words. Ussage: /sc (Message)");
			}
				
			
		}//end of perm check
		else{
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("No_Permission")));
				
		}
		
		
		
		
		
		return false;
	}

}
