package me.ryandw11.ultrachat.api;


import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.util.jsonmessage.JSONMessage;
import me.ryandw11.ultrachat.formatting.PlayerFormatting;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class Util {
	public static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + String.valueOf('§') + "[0-9A-FK-OR]");
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

	public static JSONMessage buildLore(List<?> loreLines, Player p) {
		if(loreLines == null || loreLines.size() == 0) return null;
		JSONMessage loreJson = JSONMessage.create();
		// TODO Cleanup use the then() builder methods instead of string concats
		boolean isEmpty = true;
		for(int i = 0; i < loreLines.size(); i++) {
			String loreLine = (String) loreLines.get(i);
			if(p != null) {
				loreLine = PlaceholderAPI.setPlaceholders(p, loreLine);
				// Double parse to support placeholders within luckperms meta
				loreLine = PlaceholderAPI.setPlaceholders(p, loreLine);
			}
			loreLine = ChatColor.translateAlternateColorCodes('&', loreLine);
			if(loreLine.trim().length() > 0) {
				loreJson.then(loreLine);
				// TODO Cache sequences
				if(loreLine.contains("§"+ChatColor.BOLD.getChar())) loreJson.style(ChatColor.BOLD);
				if(loreLine.contains("§"+ChatColor.MAGIC.getChar())) loreJson.style(ChatColor.MAGIC);
				if(loreLine.contains("§"+ChatColor.ITALIC.getChar())) loreJson.style(ChatColor.ITALIC);
				if(loreLine.contains("§"+ChatColor.STRIKETHROUGH.getChar())) loreJson.style(ChatColor.STRIKETHROUGH);
				if(loreLine.contains("§"+ChatColor.UNDERLINE.getChar())) loreJson.style(ChatColor.UNDERLINE);
				if(i < loreLines.size()-1)loreJson.newline();
				isEmpty = false;
			}
		}
		if(isEmpty) return null;
		else return loreJson;
	}

	public static JSONMessage buildLore(List<String> loreLines) {
		return buildLore(loreLines, null);
	}
	

}
