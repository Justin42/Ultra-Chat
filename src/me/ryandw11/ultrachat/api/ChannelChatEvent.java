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
    private final String channelName;
    private Set<Player> recipients;
    private boolean cancelled = false;
    private String scope;

    public ChannelChatEvent(Player p, Set<Player> recipients,final String channelName, String messageFormat, String messageBody) {
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

    public String getChannelName() { return channelName; }

    public String getScope() { return scope; }

    public boolean isCancelled(){ return cancelled; }

    public void setCancelled(boolean cancel){
        cancelled = cancel;
    }

    public void setScope(String scope) {  this.scope = scope; }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
