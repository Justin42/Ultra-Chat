package me.ryandw11.ultrachat.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ryandw11.ultrachat.UltraChat;
/**
 * StaffChat Toggle command.
 * @author Ryandw11
 *
 */
public class StaffChatToggle implements CommandExecutor {
	
	private UltraChat plugin;
	public StaffChatToggle(){
		plugin = UltraChat.plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {	
		if(!(sender instanceof Player)){
			plugin.getLogger().info("That command is for players only!");
			return true;
		}
		Player p = (Player) sender;
		if(p.hasPermission("ultrachat.staffchat.toggle")){
				
			if(plugin.stafftoggle.contains(p.getUniqueId())){
				plugin.stafftoggle.remove(p.getUniqueId());
				p.sendMessage(ChatColor.LIGHT_PURPLE + "Staff chat enabled!");
					
			}
			else{
				plugin.stafftoggle.add(p.getUniqueId());
				p.sendMessage(ChatColor.LIGHT_PURPLE + "Staff chat disabled!");
			}
		}//end of perm check
		return false;
	}

}