package me.ryandw11.ultrachat.formatting;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;
import me.ryandw11.ultrachat.UltraChat;
/**
 * Class for formatting player chat easily. (Demo: May not stay).
 * @author Ryandw11
 *
 */
public class PlayerFormatting {
	private UltraChat plugin;
	public PlayerFormatting(Player p){
		plugin = UltraChat.plugin;
		
		color = ChatColor.translateAlternateColorCodes('&', plugin.data.getString(p.getUniqueId() + ".color"));	
		prefix = ChatColor.translateAlternateColorCodes('&', plugin.chat.getPlayerPrefix(p));
		suffix = ChatColor.translateAlternateColorCodes('&', plugin.chat.getPlayerSuffix(p));
		formatOp = PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Custom_Chat.Op_Chat.Format")));
		defaults = PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Custom_Chat.Default_Chat.Format")));
	}
	private String prefix;
	private String suffix;
	private String color;
	private String formatOp;
	private String defaults;
	
	public String getPrefix(){
		return prefix;
	}
	public String getSuffix(){
		return suffix;
	}
	public String getColor(){
		return color;
	}
	public String getOpFormat(){
		return formatOp;
	}
	public String getDefaultFormat(){
		return defaults;
	}
	
}
