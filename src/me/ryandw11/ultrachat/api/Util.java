package me.ryandw11.ultrachat.api;


import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.util.jsonmessage.JSONMessage;
import me.ryandw11.ultrachat.formatting.PlayerFormatting;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class Util {
	/**
	 * 
	 * @param s
	 * @return
	 */
	public static ChatColor getColorFromCode(String s){
		String b = s.substring(1);
		switch (b){
		case "1":
			return ChatColor.DARK_BLUE;
		case "2":
			return ChatColor.DARK_GREEN;
		case "3":
			return ChatColor.DARK_AQUA;
		case "4":
			return ChatColor.DARK_RED;
		case "5":
			return ChatColor.DARK_PURPLE;
		case "6":
			return ChatColor.GOLD;
		case "7":
			return ChatColor.GRAY;
		case "8":
			return ChatColor.DARK_GRAY;
		case "9":
			return ChatColor.BLUE;
		case "c":
			return ChatColor.RED;
		case "d":
			return ChatColor.LIGHT_PURPLE;
		case "e":
			return ChatColor.YELLOW;
		case "f":
			return ChatColor.WHITE;
		case "a":
			return ChatColor.GREEN;
		case "b":
			return ChatColor.AQUA;
		}
		return null;
	}

	public static String buildLore(List<?> loreLines, Player p) {
		if(loreLines == null || loreLines.size() == 0) return "";
		StringBuilder loreText = new StringBuilder(100);
		for(int i = 0; i < loreLines.size(); i++) {
			String loreLine = (String) loreLines.get(i);
			if(p != null) {
				loreLine = PlaceholderAPI.setPlaceholders(p, loreLine);
			}
			loreLine = ChatColor.translateAlternateColorCodes('&', loreLine);
			if(loreLine.length() > 0) {
				loreText.append(loreLine);
			}
			if(i < loreLines.size() - 1) loreText.append("\n");
		}
		return loreText.toString();
	}

	public static String buildLore(List<String> loreLines) {
		return buildLore(loreLines, null);
	}
	

}
