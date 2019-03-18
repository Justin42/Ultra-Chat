package me.ryandw11.ultrachat.pluginhooks;

import github.scarsz.discordsrv.DiscordSRV;
import me.ryandw11.ultrachat.UltraChat;
import me.ryandw11.ultrachat.api.ChannelChatEvent;
import me.ryandw11.ultrachat.api.Lang;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.logging.Level;

public class DiscordSRVHook implements Listener {

	private UltraChat plugin;

	public DiscordSRVHook(UltraChat plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onChannelChat(ChannelChatEvent e){
		String msg = e.getMessage();
		String pname = e.getPlayer().getDisplayName();
		if(plugin.channel.getBoolean("discord", false)) {
			DiscordSRV.getPlugin().processChatMessage(e.getPlayer(), e.getMessage(), e.getChannelName(), e.isCancelled());
		}
	}

}
