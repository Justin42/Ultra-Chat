package me.ryandw11.ultrachat.formatting;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;
import me.ryandw11.ultrachat.UltraChat;
import me.ryandw11.ultrachat.api.Util;
/**
 * Class for formatting player chat easily. (Demo: May not stay).
 * @author Ryandw11
 *
 */
public class PlayerFormatting {
	private UltraChat plugin;

	private String prefix;
	private String suffix;
	public String color;
	private String formatOp;
	private boolean formatOpEnabled;
	private String defaults;
	private String global;
	private String world;
	private String local;

	public PlayerFormatting(Player p){
		plugin = UltraChat.plugin;
		
		color = ChatColor.translateAlternateColorCodes('&', plugin.data.getString(p.getUniqueId() + ".color"));	
		prefix = ChatColor.translateAlternateColorCodes('&', plugin.chat.getPlayerPrefix(p));
		suffix = ChatColor.translateAlternateColorCodes('&', plugin.chat.getPlayerSuffix(p));
		formatOp = PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Custom_Chat.Op_Chat.Format")));
		formatOpEnabled = plugin.getConfig().getBoolean("Custom_Chat.Op_Chat.Enabled", true);
		defaults = PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Custom_Chat.Default_Chat.Format")));
		global = PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Global.format")));
		world = PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("World.format")));
		local = PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Local.format")));
	}
	
	public String getGlobal(){
		return global;
	}
	
	public String getWorld(){
		return world;
	}
	
	public String getLocal(){
		return local;
	}
	
	public String getPrefix(){
		return prefix;
	}
	public String getSuffix(){
		return suffix;
	}
	public ChatColor getColor(){
		return Util.getColorFromCode(color);
	}
	public String getOpFormat(){ return formatOp; }
	public Boolean getOpFormatEnabled() { return formatOpEnabled; }
	public String getDefaultFormat(){ return defaults; }
	
}
