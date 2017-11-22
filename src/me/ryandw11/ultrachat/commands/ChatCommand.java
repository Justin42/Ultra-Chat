package me.ryandw11.ultrachat.commands;

import me.ryandw11.ultrachat.UltraChat;
import me.ryandw11.ultrachat.gui.ColorGUI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * 
 * @author Ryandw11
 *
 */
public class ChatCommand implements CommandExecutor {

	
	private UltraChat plugin;
	public ChatCommand(){
		plugin = UltraChat.plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
		if(!(sender instanceof Player )){
			plugin.getLogger().info(ChatColor.RED + "This command is for players only!");
			return true;
		}
		Player p = (Player) sender;
		
			
			if(args.length == 0){
				p.sendMessage(ChatColor.GREEN + "=============={" + ChatColor.GOLD + "Ultra Chat" + ChatColor.GREEN + "}==============");
				p.sendMessage(ChatColor.GREEN + "Plugin developed by:" + ChatColor.GOLD + " Ryandw11");
				p.sendMessage(ChatColor.GREEN + "Version: " + ChatColor.GOLD + String.format("%s", plugin.getDescription().getVersion()));
				p.sendMessage(ChatColor.GREEN + "Plugin wiki:" + ChatColor.GOLD + " https://github.com/ryandw11/Ultra-Chat/wiki");
				p.sendMessage(ChatColor.GREEN + "Do " + ChatColor.GOLD + " /chat help " + ChatColor.GREEN + "for the list of commands!");
				p.sendMessage(ChatColor.GREEN + "=============={" + ChatColor.GOLD + "Ultra Chat" + ChatColor.GREEN + "}==============");
				
			}
			
			
			//Chat Stop Command
			else if(args.length == 1 && args[0].equalsIgnoreCase("stop")){
				if(p.hasPermission("ultrachat.stopchat")){
					
					if(plugin.chatStop == true){
						plugin.chatStop = false;
						p.sendMessage(ChatColor.GREEN + "Chat Unstoped");
						Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Plugin_Prefix")) + ChatColor.translateAlternateColorCodes('&', " &eThe chat has been &2Enabled &eby: &5") + p.getDisplayName() + ChatColor.YELLOW + "!");
					}
					else if(plugin.chatStop == false){
						plugin.chatStop = true;
						p.sendMessage(ChatColor.GREEN + "Chat Stoped");
						Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Plugin_Prefix")) + ChatColor.translateAlternateColorCodes('&', " &eThe chat has been &4disabled &eby: &5") + p.getDisplayName() + ChatColor.YELLOW + "!");
					}
					else{
						p.sendMessage("An error has occured");
					}
					
					
					
				}
				else{
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("No_Permission")));
				}
				
				
			}
			
				
				else if(args.length >= 1 && args[0].equalsIgnoreCase("broadcast")){
					if(p.hasPermission("ultrachat.broadcast")){
						String Message = "";
						for (int i = 1; i < args.length; i++){
							Message = Message + " " + args[i];
						}
						Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Broadcast_Prefix")) + ChatColor.translateAlternateColorCodes('&', Message));
					}
					else{
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("No_Permission")));
					}
					
				}
				else if(args.length == 1 && args[0].equalsIgnoreCase("broadcast")){
					if(p.hasPermission("ultrachat.broadcast")){
						p.sendMessage(ChatColor.RED + "Invalid Usage: /chat broadcast (broadcast)");
					}
					else{
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("No_Permission")));
					}
				}//end of broadcast======================================================================================
			//
			//================================================================================================================
			
				else if(args.length == 1 && args[0].equalsIgnoreCase("clear")){
					if(p.hasPermission("ultrachat.clearchat")){
						for (int i = 0; i < 100; i++){
							for(Player pl : Bukkit.getOnlinePlayers()){
			                  pl.sendMessage(" ");
							}
			            }
					Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Plugin_Prefix")) + ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Clear_Chat_Message").replace("%player%", p.getDisplayName())));
					
					}
					else{
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("No_Permission")));
					}
				}
			//========================================================
			
			//==========================================================
			
				else if(args.length == 1 && args[0].equalsIgnoreCase("sjoin")){
					if(p.hasPermission("ultrachat.sjoin")){
						if(plugin.data.getBoolean(p.getUniqueId().toString() + ".sjoin")){
							p.sendMessage(ChatColor.AQUA + "Your Join/Leave message will now be shown!");
							plugin.data.set(p.getUniqueId().toString() + ".sjoin", false);
							plugin.saveFile();
						}
						else{
							plugin.data.set(p.getUniqueId().toString() + ".sjoin", true);
							plugin.saveFile();
							p.sendMessage(ChatColor.AQUA + "Your Join/Leave message will no longer be shown!"); 
						}
					}
					else{
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("No_Permission")));
					}
					
				}
				else if(args.length == 2 && args[0].equalsIgnoreCase("sjoin")){
					if(p.hasPermission("ultrachat.sjoin.others")){
						Player pl = (Player) Bukkit.getServer().getPlayer(args[1]);
						if(pl == null){p.sendMessage(ChatColor.RED + "Player can not be null!"); return true;}
						if(plugin.data.getBoolean(p.getUniqueId().toString() + ".sjoin")){
							p.sendMessage(ChatColor.AQUA + pl.getName() + " Join/Leave message will now be shown!");
							plugin.data.set(p.getUniqueId().toString() + ".sjoin", false);
							plugin.saveFile();
						}
						else{
							plugin.data.set(p.getUniqueId().toString() + ".sjoin", true);
							plugin.saveFile();
							p.sendMessage(ChatColor.AQUA + pl.getName() + " Join/Leave message will no longer be shown!"); 
						}
					}
					else{
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("No_Permission")));
					}
					
				}
			
			//======================================================================================================
			//                                      Break
			//======================================================================================================
			
			
				else if(args.length == 2 && args[0].equalsIgnoreCase("join")){
					if(p.hasPermission("ultrachat.fakejoin")){
						Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Join_Message").replace("%player%", args[1])));
						
					}
					else{
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("No_Permission")));
					}
				}//end of fake join
				
				else if(args.length == 2 && args[0].equalsIgnoreCase("leave")){
					if(p.hasPermission("ultrachat.fakeleave")){
						Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Leave_Message").replace("%player%", args[1])));
						
					}
					else{
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("No_Permission")));
					}
				}//end of fake leave
			//====================================================================
			
			//================================================================
				else if(args.length == 1 && args[0].equalsIgnoreCase("help")){
						if(!p.hasPermission("ultrachat.help"))
						p.sendMessage(ChatColor.GREEN + "-------------------=[" + ChatColor.GOLD + "Utlra Chat" + ChatColor.GREEN + "]=-------------------");
						p.sendMessage(ChatColor.GOLD + "/chat help" + ChatColor.GREEN + "  The help command.!");
						p.sendMessage(ChatColor.GOLD + "/chat stop" + ChatColor.GREEN + "  Stops the chat!");
						p.sendMessage(ChatColor.GOLD + "/chat broadcast (message)" + ChatColor.GREEN + "  Send a message to every player!");
						p.sendMessage(ChatColor.GOLD + "/sc (message)" + ChatColor.GREEN + "  Staff chat!");
						p.sendMessage(ChatColor.GOLD + "/sctoggle" + ChatColor.GREEN + "  Toggle the staff chat!");
						p.sendMessage(ChatColor.GOLD + "/spy" + ChatColor.GREEN + "  Enable or disable command spy.");
						p.sendMessage(ChatColor.GOLD + "/chat clear" + ChatColor.GREEN + "  Clear the chat.");
						p.sendMessage(ChatColor.GOLD + "/chat sjoin" + ChatColor.GREEN + "  When you leave you join the game it won't show that you logged in.");
						p.sendMessage(ChatColor.GOLD + "/chat leave (player)" + ChatColor.GREEN + "  Have a fake leave.");
						p.sendMessage(ChatColor.GOLD + "/chat join (player) " + ChatColor.GREEN + "  Have a fake join.");
						p.sendMessage(ChatColor.GREEN + "Do" + ChatColor.GOLD + " /chat help 2" + ChatColor.GREEN + " for more help pages!");
						p.sendMessage(ChatColor.GREEN + "Plugin made by: " + ChatColor.GOLD + "Ryandw11" + ChatColor.GREEN + "! Help Page: " + ChatColor.GOLD + "1/2" + ChatColor.GREEN + ".");
						p.sendMessage(ChatColor.GREEN + "---------------------------------------------------");
					
				}//end of  help
				
				else if(args.length == 2 && args[0].equalsIgnoreCase("help")){
					if(p.hasPermission("ultrachat.help")){
						if(args[1].equals("2")){
							p.sendMessage(ChatColor.GREEN + "-------------------=[" + ChatColor.GOLD + "Utlra Chat" + ChatColor.GREEN + "]=-------------------");
							p.sendMessage(ChatColor.GOLD + "/chat raw {Message}" + ChatColor.GREEN + "  Send a message in the chat without a prefix.");
							p.sendMessage(ChatColor.GOLD + "/chat reload" + ChatColor.GREEN + "  Reload the config file!");
							p.sendMessage(ChatColor.GREEN + "Plugin made by: " + ChatColor.GOLD + "Ryandw11" + ChatColor.GREEN + "! Help Page: " + ChatColor.GOLD + "2/2" + ChatColor.GREEN + ".");
							p.sendMessage(ChatColor.GREEN + "---------------------------------------------------");
						}
						if(!(args[1].equals("1") || args[1].equals("2"))){
							p.sendMessage(ChatColor.RED + "There are only two help pages!");
						}
					}
					else{
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("No_Permission")));
					}
				}//end of help page ========
			
				else if(args.length > 1 && args[0].equalsIgnoreCase("raw")){
					if(p.hasPermission("ultrachat.raw")){
						String Message = "";
						for (int i = 1; i < args.length; i++){
							Message = Message + " " + args[i];
						}
						Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.translateAlternateColorCodes('&', Message)));
					}
					else{
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("No_Permission")));
					}
				}
			
				else if(args.length == 1 && args[0].equalsIgnoreCase("reload")){
					if(p.hasPermission("ultrachat.reload")){
						plugin.reloadConfig();
						plugin.saveChannel();
						plugin.loadChannel();
						p.sendMessage(ChatColor.GREEN + "The config has been reloaded!");
					}
					else{
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("No_Permission")));
					}
				}
				else if(args.length == 1 && args[0].equalsIgnoreCase("color")){
					if(p.hasPermission("ultrachat.color")){
						ColorGUI.openGUI(p.getPlayer());
					}
					else{
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("No_Permission")));
					}
				}
//=========================================----------------------------------------===========================================
			else{
				p.sendMessage(ChatColor.RED + "That is not a valid command do /chat for help.");
			}
		
		return false;
	}

}
