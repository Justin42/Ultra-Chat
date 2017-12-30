package me.ryandw11.ultrachat;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import me.ryandw11.ultrachat.commands.ChannelCmd;
import me.ryandw11.ultrachat.commands.ChatCommand;
import me.ryandw11.ultrachat.commands.StaffChat;
import me.ryandw11.ultrachat.commands.StaffChatToggle;
import me.ryandw11.ultrachat.commands.SpyCommand;
import me.ryandw11.ultrachat.formatting.Channels;
import me.ryandw11.ultrachat.formatting.Chat_Json;
import me.ryandw11.ultrachat.formatting.Normal;
import me.ryandw11.ultrachat.gui.ColorGUI;
import me.ryandw11.ultrachat.listner.JoinListner;
import me.ryandw11.ultrachat.listner.NoSwear;
import me.ryandw11.ultrachat.listner.Notify;
import me.ryandw11.ultrachat.listner.Spy;
import me.ryandw11.ultrachat.listner.StopChat;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
/**
 * Main Class
 * @author Ryandw11
 * @version 2.0
 */
public class UltraChat extends JavaPlugin{
	
	public static UltraChat plugin;
	public Permission perms = null;
	public Chat chat = null;
	public Boolean Vault;
	public Boolean chatStop = false;
	public Boolean channelEnabled;
	public Boolean JSON;
	public String defaultChannel;
	 
	public File datafile = new File(getDataFolder() + "/data/players.yml");
	public FileConfiguration data = YamlConfiguration.loadConfiguration(datafile);
	public File channelfile;
	public FileConfiguration channel;
	public String prefix;

	

	@Override
	public void onEnable(){

		
		/*
		 * Plugin setup area
		 */
		plugin = this;
		 if (getServer().getPluginManager().getPlugin("Vault") == null) {
			 	getLogger().info(String.format("[%s] - Vault is not found!", getDescription().getName()));
				getLogger().severe("[UltraChat] Warning: You do not have Vault installed! This plugin has been disabled!");
				Bukkit.getPluginManager().disablePlugin(this);
				return;
	        }
		 if (getServer().getPluginManager().getPlugin("PlaceholderAPI") == null) {
			 getLogger().severe("[UltraChat] Warning: You do not have PlaceholderAPI installed! This plugin has been disabled!");
	          Bukkit.getPluginManager().disablePlugin(this);
	          return;
		 }
		 else{
			 getLogger().info(String.format("[UltraChat] is enabled and running fine! V: %s", getDescription().getVersion())); 
			 getLogger().info("[UltraChat] Hooked into PlaceholderAPI! You can use the place holders!");
		 }
		loadMethod();
		registerConfig();
		loadFile();
		loadChannel();

		setupPermissions();
		setupChat();
		setupFormatting();
	}
	
	@Override
	public void onDisable(){
		getLogger().info("[UltraChat] has been disabled correctly!");
		saveFile();
		saveChannel();
	}
	/**
	 * Setup the chat formatting.
	 */
	public void setupFormatting(){
		channelEnabled = getConfig().getBoolean("Channels");
		if(channelEnabled){
			if(legitDefaultChannel(getConfig().getString("Default_Channel"))){
				defaultChannel = getConfig().getString("Default_Channel");
			}
			else{
				channelEnabled = false;
			}

		}
		if(getConfig().getBoolean("JSON")){
			JSON = true;
		}
		else{
			JSON = false;
		}
		if(!getConfig().getBoolean("Custom_Chat_Enabled")){
			getLogger().info("Custom chat is not enabled. The chat will not be modified!");
		}
		if(JSON){
			Bukkit.getServer().getPluginManager().registerEvents(new Chat_Json(), this);
			
		}else if(channelEnabled){
			Bukkit.getServer().getPluginManager().registerEvents(new Channels(), this);
		}else{
			Bukkit.getServer().getPluginManager().registerEvents(new Normal(), this);
		}
	}
	
	//Vault set-up =========================================================

	private boolean setupChat() {
	        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
	        chat = rsp.getProvider();
	        return chat != null;
	}

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }
    //========================================================================= END ===============================
    /**
     * Save the data file.
     */
    public void saveFile(){
		
		try{
			data.save(datafile);
		}catch(IOException e){
			e.printStackTrace();
			
		}
		
	}
	/**
	 * load the data file
	 */
	public void loadFile(){
		if(datafile.exists()){
			try {
				data.load(datafile);
				
			} catch (IOException | InvalidConfigurationException e) {

				e.printStackTrace();
			}
		}
		else{
			try {
				data.save(datafile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * Save the channel file.
	 */
    public void saveChannel(){
		
		try{
			channel.save(channelfile);
		}catch(IOException e){
			e.printStackTrace();
			
		}
		
	}
	
	/**
	 * Load the cannel file.
	 */
	public void loadChannel(){
		 channelfile = new File(getDataFolder(), "channel.yml");
		 if (!channelfile.exists()) {
	            channelfile.getParentFile().mkdirs();
	            saveResource("channel.yml", false);
	         }
		 channel = new YamlConfiguration();
		 try {
				channel.load(channelfile);
				
			} catch (IOException | InvalidConfigurationException e) {

				e.printStackTrace();
			}
	}
	
	
	
    
	
	
	private void registerConfig() {
		saveDefaultConfig();
	}
	/**
	 * See if default channel exists.
	 * @param chan - The channel in the config.
	 * @return True if it does, false if not.
	 */
	private boolean legitDefaultChannel(String chan){
		if(channel.contains(chan)){
			return true;
		}
		else{
			getLogger().warning("Error: The channel, " + chan + ", does not exsist!");
			getLogger().warning("Please fix this by going into the config and fixing the default channel.");
			return false;
		}
	}
	/**
	 * Loads all of the Events and Commands.
	 */
	public void loadMethod(){
		getCommand("chat").setExecutor(new ChatCommand(this));
		getCommand("sc").setExecutor(new StaffChat(this));
		getCommand("sctoggle").setExecutor(new StaffChatToggle(this));
		getCommand("spy").setExecutor(new SpyCommand(this));
		if(!(plugin.getConfig().getBoolean("ChatColor_Command")))
			getCommand("color").setExecutor(new ColorGUI(this));
		getCommand("channel").setExecutor(new ChannelCmd(this));
		Bukkit.getServer().getPluginManager().registerEvents(new StopChat(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(new NoSwear(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(new Spy(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(new JoinListner(this), this);
		//Bukkit.getServer().getPluginManager().registerEvents(new Format(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(new ColorGUI(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(new Notify(this), this);
		
		this.prefix = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Plugin_Prefix"));
		
	}


}

//Permmisions:
//ultrachat.stopchat
//ultrachat.stopchat.bypass
//ultrachat.mode
//ultrachat.vipchat
//ultrachat.broadcast
//ultrachat.staffchat
//ultrachat.staffchat.toggle
//ultrachat.spy
//ultrachat.staffmode
//ultrachat.clearchat
//ultrachat.commandblock.bypass
//ultrachat.sjoin
//ultrachat.color
//ultrachat.worldmute
//ultrachat.sjoin.alert
//ultrachat.spy.others
//ultrachat.fakejoin
//ultrachat.fakeleave
//ultrachat.help