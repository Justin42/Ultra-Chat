package me.ryandw11.ultrachat.api;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.util.jsonmessage.JSONMessage;
import me.ryandw11.ultrachat.formatting.PlayerFormatting;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

//import me.ryandw11.ultrachat.UltraChat;


public class JSON {
	
	//private UltraChat plugin;
	public JSON(){
		//plugin = UltraChat.plugin;
	}
	
	/**
	 * Builds the hover message.
	 * @param msg The play name and format around it.
	 * @param lore The lore of the text.
	 * @param p The player it is being applied to.
	 * @return TextComponent with hover message.
	 */
	public JSONMessage hoverMessage(String msg, ArrayList<String> lore, String chat, ChatColor color, Player p){
		PlayerFormatting pf = new PlayerFormatting(p);
		int i = 0;
		int l = lore.size() - 1;
		msg = ChatColor.translateAlternateColorCodes('&', msg);
		JSONMessage chatMessage = JSONMessage.create(msg);
		JSONMessage hoverTooltip = JSONMessage.create();
		for(String s : lore){
			hoverTooltip.then(PlaceholderAPI.setPlaceholders(p, ChatColor.translateAlternateColorCodes('&', s)));
			if(i<l)
				hoverTooltip.newline();
			i++;
		}
		chatMessage.tooltip(hoverTooltip);
		if(color != null){
			chatMessage.then(chat).color(color);
		}else{
			String smgsd = ChatColor.translateAlternateColorCodes('&', pf.color + chat);
			chatMessage.then(smgsd);
		}
		chatMessage.then(this.getMsg(chat, color, p));
		return chatMessage;
	}
	/**
	 * Format the chat.
	 * @param chat The player message.
	 * @param color The player's chat color
	 * @return Chat message with string.
	 */
	public String getMsg(String chat, ChatColor color, Player p){
		String msg = "";
		if(color != null){
			if(p.hasPermission("ultrachat.chat.color")){
				msg = ChatColor.translateAlternateColorCodes('&', color + chat);
			}else{
				msg = ChatColor.translateAlternateColorCodes('&', color + chat);
			}
		}else{
			msg = ChatColor.translateAlternateColorCodes('&', chat);
		}
		return msg;
	}
}
