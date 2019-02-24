package me.ryandw11.ultrachat.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Set;

/**
 * Event class
 * @author Ryandw11
 *
 */
public class ChannelChatEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private String messageBody;
    private String messageFormat;
    private String channelName;
    private Set<Player> recipients;
    private boolean cancelled;

    public ChannelChatEvent(Player p, Set<Player> recipients, String channelName, String messageFormat, String messageBody) {
        this.player = p;
        this.recipients = recipients;
        this.channelName = channelName;
        this.messageFormat = messageFormat;
        this.messageBody = messageBody;
    }

    public Player getPlayer() { return player; }

    public Set<Player> getRecipients() { return recipients; }

    public void setRecipients(Set<Player> recipients) { this.recipients = recipients; }

    public String getMessage() { return messageBody; }

    public void setMessage(String message) { this.messageBody = message; }

    public void setMessageFormat(String format) { this.messageFormat = format; }

    public String getMessageFormat() { return this.messageFormat;}

    public void getChannelName() { this.channelName = channelName; }

    public boolean isCancelled(){ return cancelled; }

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
