package me.ryandw11.ultrachat.commands;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ryandw11.ultrachat.UltraChat;

public class StaffChat implements CommandExecutor {

public static ArrayList<UUID> stafftoggle = new ArrayList<>();
	

	private UltraChat plugin;
	
	public StaffChat(UltraChat plugin){
		this.plugin = plugin;
	}



	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
		
		Player p = (Player) sender;
		if(cmd.getName().equalsIgnoreCase("sc")){
			if(p.hasPermission("ultrachat.staffchat") || p.isOp()){
				String Message = "";
				
				if(args.length > 0){
				
				for (int i = 0; i < args.length; i++){
					Message = Message + " " + args[i];
					
				}
				for(Player p1 : Bukkit.getOnlinePlayers()){
					if(p1.hasPermission("ultrachat.staffchat") || p1.isOp()){
					if(stafftoggle.contains(p1.getUniqueId())){
						
					}
					else{
					p1.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Staff_Chat_Prefix")) + ChatColor.DARK_AQUA + p.getName() + ":" + ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Staff_Chat_Color")) + Message);
					
					
					}// end of if
				}
				}//end of for
				}//end
				else{
					p.sendMessage(ChatColor.RED + "Not enough words. Ussage: /sc (Message)");
				}
				
			//end of perm check
			}
			else{
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("No_Permission")));
				
			}
		}
		
		
		
		
		return false;
	}

}
