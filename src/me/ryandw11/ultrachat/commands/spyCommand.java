package me.ryandw11.ultrachat.commands;
import java.util.ArrayList;
import java.util.UUID;





import me.ryandw11.ultrachat.core.UltraChat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class spyCommand implements CommandExecutor {
	
	public static ArrayList<UUID> spytoggle = new ArrayList<>();

	@SuppressWarnings("unused")
	private UltraChat plugin;
	public spyCommand(UltraChat plugin){
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
		
		Player p = (Player) sender;
			if(!(p instanceof Player )) return true;
			if(args.length == 0){
			if (p.hasPermission("ultrachat.spy") || p.isOp()) {
				if(!(args.length == 0)){
					p.sendMessage(ChatColor.RED + "Invailed subcommand! Ussage: /spy");
					
				}
				else{
					if(spytoggle.contains(p.getUniqueId())){
						p.sendMessage(plugin.prefix + ChatColor.AQUA + "Command Spy Disabled!");
						spytoggle.remove(p.getUniqueId());
						plugin.data.set(p.getUniqueId().toString() + ".spy", false);
						plugin.saveFile();
					}
					else{
						spytoggle.add(p.getUniqueId());
						p.sendMessage(plugin.prefix + ChatColor.AQUA + "Command Spy Enabled"); 
						plugin.data.set(p.getUniqueId().toString() + ".spy", true);
						plugin.saveFile();
					}
				}
				
				
				
				
			}//end of perm check
			}
			else if(args.length == 1){
				if(p.hasPermission("ultrachat.spy.others")){
					Player pl = (Player) Bukkit.getServer().getPlayer(args[0]);
					if(spytoggle.contains(pl.getUniqueId())){
						spytoggle.remove(pl.getUniqueId());
						p.sendMessage(plugin.prefix + ChatColor.BLUE + args[0] + " spy has been disabled!");
						plugin.data.set(pl.getUniqueId().toString() + ".spy", false);
						plugin.saveFile();
					}
					else{
						spytoggle.add(pl.getUniqueId());
						p.sendMessage(plugin.prefix + ChatColor.BLUE + args[0] + " spy has been enabled!");
						plugin.data.set(pl.getUniqueId().toString() + ".spy", true);
						plugin.saveFile();
					}
				}
			}
			
			
			
		

		
		
		
		
		return false;
	}

}
