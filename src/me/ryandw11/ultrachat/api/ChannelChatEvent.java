package me.ryandw11.ultrachat.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Set;

/**
 * Event class
 * @author Ryandw11
 *
 */
public class ChannelChatEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private String chat;
    private boolean cancelled;

    public ChannelChatEvent(Player p, String channel, String chat) {
        player = p;
        this.chat = chat;
    }

    public Player getPlayer() {
        return player;
    }

    public String getMessage() {
        return chat;
    }

    public void setMessage(String message){
        chat = message;
    }

    public boolean isCancelled(){
        return cancelled;
    }

    public void setCancelled(boolean cancel){
        cancelled = cancel;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
