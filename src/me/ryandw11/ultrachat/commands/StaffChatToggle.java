package me.ryandw11.ultrachat.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ryandw11.ultrachat.UltraChat;

public class StaffChatToggle implements CommandExecutor {
	
@SuppressWarnings("unused")
private UltraChat plugin;
	
	public StaffChatToggle(UltraChat plugin){
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String s, String[] arg3s) {
		
		Player p = (Player) sender;
		if(cmd.getName().equalsIgnoreCase("sctoggle")){
			if(!(p instanceof Player)) return true;
			if(p.hasPermission("ultrachat.staffchat.toggle") || p.isOp()){
				
				if(StaffChat.stafftoggle.contains(p.getUniqueId())){
					StaffChat.stafftoggle.remove(p.getUniqueId());
					p.sendMessage(ChatColor.LIGHT_PURPLE + "Staff chat enabled!");
					
				}
				else{
					StaffChat.stafftoggle.add(p.getUniqueId());
					p.sendMessage(ChatColor.LIGHT_PURPLE + "Staff chat disabled!");
				}
				
				
				
				
				
				
			}//end of perm check
			
		}//end of name check
		
		
		return false;
	}

}