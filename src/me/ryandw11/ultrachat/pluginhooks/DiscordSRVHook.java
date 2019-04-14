package me.ryandw11.ultrachat.pluginhooks;

import github.scarsz.discordsrv.DiscordSRV;
import me.ryandw11.ultrachat.UltraChat;
import me.ryandw11.ultrachat.api.ChannelChatEvent;
import me.ryandw11.ultrachat.api.UltraChatHookType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class DiscordSRVHook implements Listener {

	private UltraChat plugin;

	public DiscordSRVHook(UltraChat plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onChannelChat(ChannelChatEvent e){
		if(plugin.getAPI().isHookActive(UltraChatHookType.DiscordSRV) && plugin.channel.getBoolean(e.getChannelName() + ".discord", false)) {
			//plugin.getLogger().info(String.format("Forwarding channel message from %s to discord.", e.getChannelName()));
			DiscordSRV.getPlugin().processChatMessage(e.getPlayer(), e.getMessage(), e.getChannelName(), e.isCancelled());
		}
	}

}
