package me.ryandw11.ultrachat.listener;

import java.util.logging.Level;

import me.ryandw11.ultrachat.UltraChat;
import me.ryandw11.ultrachat.api.ChannelChatEvent;
import me.ryandw11.ultrachat.api.UltraChatAPI;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.ryandw11.ultrachat.api.JsonChatEvent;
import me.ryandw11.ultrachat.api.Lang;

public class ConsoleLogChat implements Listener{

	private UltraChat plugin;

	public ConsoleLogChat(UltraChat plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void jsonChat(ChannelChatEvent e){
		String msg = e.getMessage();
		String pname = e.getPlayer().getDisplayName();
		Bukkit.getLogger().log(Level.INFO, Lang.CONSOLE_CHANNEL_CHAT_LOG.toString().replace("%c", plugin.channel.getString(e.getChannelName() + ".prefix")).replace("%p", e.getPlayer().getName()).replace("%s", e.getMessage()).replace('&', '§'));
	}

}
