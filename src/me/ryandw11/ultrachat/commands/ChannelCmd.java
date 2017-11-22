package me.ryandw11.ultrachat.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ryandw11.ultrachat.UltraChat;
/**
 * 
 * @author Ryandw11
 *
 */
public class ChannelCmd implements CommandExecutor {
	private UltraChat plugin;
	public ChannelCmd(){
		plugin = UltraChat.plugin;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {


		Player p = (Player) sender;
		if(!(p instanceof Player )) return true;
		if(!p.hasPermission("ultrachat.channel")){
			p.sendMessage(ChatColor.RED + "You do not have permission for that command!");
			return true;
		}
		if(!plugin.channelEnabled){
			p.sendMessage(ChatColor.RED + "Error: Chat channels are not enabled on this server!");
			return true;
		}
		if(args.length != 1 && args.length != 0){
			p.sendMessage(ChatColor.RED + "Error: Use /channel {channel}!");
		}
		else if(args.length == 0){
			p.sendMessage(ChatColor.BLUE + "-------------Chat Channels--------------");
			p.sendMessage(ChatColor.GREEN + "/channel {channel} - Change you channel. [Case Sensitive!]");
		}
		else{
			if(!plugin.channel.contains(args[0])){
				p.sendMessage(ChatColor.RED + "That channel does not exsist!");
				return true;
			}
			if(plugin.data.getString(p.getUniqueId() + ".channel").equalsIgnoreCase(args[0])){
				p.sendMessage(ChatColor.RED + "Error: you are currently in the channel");
			}
			else if(p.hasPermission(plugin.channel.getString(args[0] + ".permission")) || plugin.channel.getString(args[0] + ".permission").equalsIgnoreCase("none")){
				plugin.data.set(p.getUniqueId() + ".channel", args[0]);
				plugin.saveFile();
				p.sendMessage(ChatColor.BLUE + "You are now in the channel " + args[0] + "!");
			}
			else{
				p.sendMessage(ChatColor.RED + "You do not have permission to join that channel.");
			}
		}
		return false;
	}

}
