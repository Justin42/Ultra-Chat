package me.ryandw11.ultrachat.api;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.ryandw11.ultrachat.UltraChat;
/**
 * UltraChatAPI
 * @author Ryandw11
 * @version 2.1.0
 */

public class UltraChatAPI{
	private UltraChat plugin;
	private EnumMap<UltraChatHookType, String> hookPluginName;

	/*
	 * 
	 * 		UltraChatAPI ch = new UltraChatAPI(UltraChat.plugin); < Method
	 * 
	 * 
	 * 
	 */
	public UltraChatAPI(UltraChat plugin){

		this.plugin = plugin;
		this.setHookPluginNames();
	}

	protected void setHookPluginNames() {
		hookPluginName = new EnumMap<>(UltraChatHookType.class);
		hookPluginName.put(UltraChatHookType.AdvancedBan, "AdvancedBan");
		hookPluginName.put(UltraChatHookType.Essentials, "Essentials");
		hookPluginName.put(UltraChatHookType.DiscordSRV, "DiscordSRV");
		hookPluginName.put(UltraChatHookType.Towny, "Towny");
	}


	/**
	 * Grab the Instance
	 * @return UltraChatAPI instance
	 */
	public static UltraChatAPI getInstance(){
		return UltraChat.plugin.getAPI();
	}

	/**
	 * Grab the player's current channel.
	 * @param player
	 * @return The player's current channel.
	 */
	public String getPlayerChannel(Player player){
		return plugin.data.getString(player.getUniqueId() + ".channel");
	}

	/**
	 * Get the servers default channel.
	 * @return Default Channel
	 */
	public String getDefaultChannel(){
		return plugin.getConfig().getString("Default_Channel");
	}

	/**
	 * Set the player's channel.
	 * @param player
	 * @param channel
	 */
	public void setPlayerChannel(Player player, String channel){
		plugin.data.set(player.getUniqueId() + ".channel", channel);
		plugin.saveFile();
	}

	/**
	 * Set the default channel
	 * @param channel
	 */
	public void setDefaultChannel(String channel){
		plugin.getConfig().set("Default_Config", channel);
		plugin.saveConfig();
	}

	/**
	 * Get a channel's json.
	 * @param channel
	 * @return Json array
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<String> getChannelJson(String channel){
		return (ArrayList<String>) plugin.channel.get(channel + ".JSON");
	}

	/**
	 * Get a format's json.
	 * @param number
	 * @return Json array
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<String> getChatFormatJson(String number){
		return (ArrayList<String>) plugin.getConfig().get("Custom_Chat." + number + ".JSON");
	}

	/**
	 * Set a channel's json.
	 * @param json
	 * @param channel
	 */
	public void setChannelJson(ArrayList<String> json, String channel){
		plugin.channel.set(channel + ".json", json);
		plugin.saveChannel();
	}

	/**
	 * Set a format's json.
	 * @param json
	 * @param number
	 * 
	 */
	public void setChatFormatJson(ArrayList<String> json, String number){
		plugin.getConfig().set("Custom_Chat." + number + ".JSON", json);
		plugin.saveConfig();
	}

	/**
	 * Check to see if channels are enabled.
	 * @return boolean
	 */
	public boolean isChannelEnabled(){
		return plugin.getConfig().getBoolean("ChannelChatListener");
	}

	/**
	 * Check to see if json is enabled.
	 * @return boolean
	 */
	public boolean isJsonEnabled(){
		return plugin.getConfig().getBoolean("JSON");
	}

	/**
	 * Get a chat format.
	 * @param number
	 * @return Chat format.
	 */
	public String getFormat(String number){
		return plugin.getConfig().getString("Custom_Chat." + number + ".Format");
	}

	/**
	 * Get the op format.
	 * @return op format.
	 */
	public String getOpFormat(){
		return plugin.getConfig().getString("Custom_Chat.Op_Chat.Format");
	}

	/**
	 * Get the default format.
	 * @return op format.
	 */
	public String getDefaultFormat(){
		return plugin.getConfig().getString("Custom_Chat.Default.Format");
	}

	/**
	 * Get the permission of a chat group.
	 * @param number
	 * @return permission
	 */
	public String getPermission(String number){
		return plugin.getConfig().getString("Custom_Chat." + number + ".Permission");
	}

	/**
	 * Get the number of formats
	 * @return Chat Count
	 */
	public int getChatCount(){
		return plugin.getConfig().getInt("Custom_Chat.Chat_Count");
	}

	/**
	 * Get a player's color. Example: &4
	 * @param player
	 * @return color code.
	 */
	public String getPlayerColor(Player player){
		return plugin.data.getString(player.getUniqueId() + ".color");
	}

	/**
	 * Set a player's color.
	 * @param player
	 * @param color
	 */
	public void setPlayerColor(Player player, String color){
		plugin.data.set(player.getUniqueId() + ".color", color);
		plugin.saveFile();
	}

	/**
	 * Get the swear word list.
	 * @return Block swear words.
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<String> getSwearWords(){
		return (ArrayList<String>) plugin.getConfig().get("Blocked_Words");
	}

	/**
	 * Set the swear word list.
	 * @param words
	 */
	public void setSwearWords(ArrayList<String> words){
		plugin.getConfig().set("Blocked_Words", words);
		plugin.saveConfig();
	}

	/**
	 * Create a channel.
	 * @param channel
	 * @param prefix
	 * @param permission
	 * @param always_appear
	 * @param format
	 * @param json
	 */
	public void createChannel(String channel, String prefix, String permission, boolean always_appear, String format, ArrayList<String> json){
		plugin.channel.set(channel + ".prefix", prefix);
		plugin.channel.set(channel + ".permission", permission);
		plugin.channel.set(channel + ".always_appear", always_appear);
		plugin.channel.set(channel + ".format", format);
		plugin.channel.set(channel + ".JSON", json);
		plugin.saveChannel();
	}

	/**
	 * Remove a channel.
	 * @param channel
	 */
	public void removeChannel(String channel){
		plugin.channel.set(channel, null);
		plugin.saveChannel();
	}
	
	/**
	 * Grab the plugin.
	 * @return plugin. Don't worry about the return
	 */
	public static UltraChat getPlugin(){
		return UltraChat.plugin;
	}

	/**
	 * Get the current formatting type.
	 * @return The value of the config.
	 */
	public String getFormattingType(){
		return plugin.getConfig().getString("chat_format");
	}

	/**
	 * Get if the channel has json enabled or not.
	 * @return True or false.
	 */
	public boolean isChannelJson(){
		return plugin.getConfig().getBoolean("Channel_Json");
	}
	
	/**
	 * See if default channel exists.
	 * @param chan - The channel in the config.
	 * @return True if it does, false if not.
	 */
	public boolean legitDefaultChannel(String chan){
		if(plugin.channel.contains(chan))
			return true;
		return false;
	}

	/**
	 * Get if range has json or not.
	 * @return True or False.
	 */
	public boolean isRangeJson(){
		return plugin.getConfig().getBoolean("Range_Json");
	}

	/**
	 * Get the current active hooks.
	 * @return The set with the names of the plugins. Returns null if no hooks are active.
	 */
	public Set<String> getActiveHooks(){
		Set<String> s = new HashSet<String>();
		if(Bukkit.getServer().getPluginManager().isPluginEnabled("Essentials") && plugin.getConfig().getBoolean("pluginhooks.Essentials")){
			s.add("Essentials");
		}
		if(Bukkit.getServer().getPluginManager().isPluginEnabled("AdvancedBan") && plugin.getConfig().getBoolean("pluginhooks.AdvancedBan")){
			s.add("AdvancedBan");
		}
		if(Bukkit.getServer().getPluginManager().isPluginEnabled("Towny") && plugin.getConfig().getBoolean("pluginhooks.Towny")){
			s.add("Towny");
		}
		if(Bukkit.getServer().getPluginManager().isPluginEnabled("DiscordSRV") && plugin.getConfig().getBoolean("pluginhooks.DiscordSRV")){
			s.add("DiscordSRV");
		}
		
		return s;
	}

	public boolean isHookActive(UltraChatHookType hookType) {
		String pluginName = hookPluginName.get(hookType);
		if(pluginName != null) {
			return Bukkit.getServer().getPluginManager().isPluginEnabled(pluginName)
					&& plugin.getConfig().getBoolean("pluginhooks." + pluginName);
		}
		return false;
	}
}
