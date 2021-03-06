package me.ryandw11.ultrachat;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.UUID;

import com.iwebpp.crypto.TweetNaclFast;
import com.palmergames.bukkit.towny.Towny;
import github.scarsz.discordsrv.hooks.chat.TownyChatHook;
import me.ryandw11.ultrachat.api.Lang;
import me.ryandw11.ultrachat.api.UltraChatAPI;
import me.ryandw11.ultrachat.commands.ChannelCmd;
import me.ryandw11.ultrachat.commands.ChatCommand;
import me.ryandw11.ultrachat.commands.CommandTabCompleter;
import me.ryandw11.ultrachat.commands.Global;
import me.ryandw11.ultrachat.commands.StaffChat;
import me.ryandw11.ultrachat.commands.StaffChatToggle;
import me.ryandw11.ultrachat.commands.World;
import me.ryandw11.ultrachat.commands.SpyCommand;
import me.ryandw11.ultrachat.listener.*;
import me.ryandw11.ultrachat.listener.ChannelChatListener;
import me.ryandw11.ultrachat.formatting.Chat_Json;
import me.ryandw11.ultrachat.formatting.Normal;
import me.ryandw11.ultrachat.formatting.Range;
import me.ryandw11.ultrachat.gui.ColorGUI;
import me.ryandw11.ultrachat.pluginhooks.AdvancedBanMute;
import me.ryandw11.ultrachat.pluginhooks.DiscordSRVHook;
import me.ryandw11.ultrachat.pluginhooks.EssentialsMute;
import me.ryandw11.ultrachat.util.Metrics;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
/**
 * Main Class
 * @author Ryandw11
 * @version 2.2
 * Updated for 1.13.
 * (Finally updated the yamlconfig)
 * 
 */
public class UltraChat extends JavaPlugin{
	
	public static UltraChat plugin;
	public Permission perms = null;
	public Chat chat = null;
	public Boolean chatStop = false;
	public Boolean channelEnabled = false;
	public Boolean JSON = false;
	public ArrayList<UUID> stafftoggle = new ArrayList<>();
	public ArrayList<UUID> spytoggle = new ArrayList<>();
	 
	public File datafile = new File(getDataFolder() + "/data/players.yml");
	public FileConfiguration data = YamlConfiguration.loadConfiguration(datafile);
	public File channelfile;
	public FileConfiguration channel;
	public String prefix;
	public static YamlConfiguration LANG;
	public static File LANG_FILE;

	protected UltraChatAPI api;

	@Override
	public void onEnable(){

		
		/*
		 * Plugin setup area
		 */
		plugin = this;
		api = new UltraChatAPI(this);
		 if (getServer().getPluginManager().getPlugin("Vault") == null && !setupChat()) {
			 	getLogger().info(String.format("[%s] - Vault is not found!", getDescription().getName()));
				getLogger().severe("�cWarning: You do not have Vault installed! This plugin has been disabled!");
				Bukkit.getPluginManager().disablePlugin(this);
				return;
	        }
		 if (getServer().getPluginManager().getPlugin("PlaceholderAPI") == null) {
			 getLogger().severe("�cWarning: You do not have PlaceholderAPI installed! This plugin has been disabled!");
	          Bukkit.getPluginManager().disablePlugin(this);
	          return;
		 }
		 else{
			 getLogger().info(String.format("UltraChat is enabled and running fine! V: %s", getDescription().getVersion())); 
			 getLogger().info("Hooked into PlaceholderAPI! You can use the place holders!");
		 }
		 if(getServer().getPluginManager().getPlugin("AdvancedBan") != null && getConfig().getBoolean("pluginhooks.AdvancedBan", false)){
			 getLogger().info("AdvancedBan detected! Activating hook!");
			 getLogger().info("Mutes will now work with the chat types.");
			 Bukkit.getServer().getPluginManager().registerEvents(new AdvancedBanMute(), this);
		 }
		 if(getServer().getPluginManager().getPlugin("Essentials") != null && getConfig().getBoolean("pluginhooks.Essentials", false)){
			 getLogger().info("Essentials detected! Activating hook!");
			 getLogger().info("Mutes will now work with the chat types.");
			 Bukkit.getServer().getPluginManager().registerEvents(new EssentialsMute(), this);
		 }
		if(getServer().getPluginManager().getPlugin("DiscordSRV") != null && getConfig().getBoolean("pluginhooks.DiscordSRV", false)){
			getLogger().info("DiscordSRV detected! Activating hook!");
			getLogger().info("Set 'discord: true' in channel configuration for Discord forwarding.");
			Bukkit.getServer().getPluginManager().registerEvents(new DiscordSRVHook(this), this);
		}
		if(getServer().getPluginManager().getPlugin("Towny") != null && getConfig().getBoolean("pluginhooks.Towny", false)) {
			getLogger().info("Hooked into Towny!");
			getLogger().info("Enabled 'town' and 'nation' channel scopes");
			Bukkit.getServer().getPluginManager().registerEvents(new TownyChatHook(), this);
		}
		loadMethod();
		registerConfig();
		loadFile();
		loadChannel();

		setupPermissions();
		setupChat();
		setupFormatting();
		loadLang();
		if(plugin.getConfig().getBoolean("bstats")){
			@SuppressWarnings("unused")
			Metrics m = new Metrics(this);
		}
	}

	public UltraChatAPI getAPI() {
		return this.api;
	}

	@Override
	public void onDisable(){
		saveFile();
		saveChannel();
		getLogger().info("[UltraChat] has been disabled correctly!");
	}
	/**
	 * Setup the chat formatting.
	 */
	public void setupFormatting(){
		String type = getConfig().getString("chat_format");
		if(type.equals("")){
			getLogger().info("UltraChat will not format the chat. To change this go into the config.");
			return;
		}
		switch(type.toLowerCase()){
		case "normal":
			Bukkit.getServer().getPluginManager().registerEvents(new Normal(), this);
			JSON = false;
			channelEnabled = false;
			getLogger().info("Normal chat mode activated!");
			break;
		case "json":
			Bukkit.getServer().getPluginManager().registerEvents(new Chat_Json(), this);
			JSON = true;
			channelEnabled = false;
			getLogger().info("Json chat activated!");
			break;
		case "channel":
			channelEnabled = true;
			JSON = true;
			Bukkit.getServer().getPluginManager().registerEvents(new ChannelChatListener(), this);
			getLogger().info("Channel chat mode enabled.");
			break;
		case "range":
			Bukkit.getServer().getPluginManager().registerEvents(new Range(), this);
			getCommand("global").setExecutor(new Global());
			getCommand("world").setExecutor(new World());
			getLogger().info("Range chat mode enabled. The commands /global and /world are now also active.");
			break;
		default:
			getLogger().warning("�cThe chat format value is not correct!");
			getLogger().warning("�cIt most be one of the following: Normal, Json, Channel, Range. (Caps do not matter)");
			getLogger().warning("�cNo formatting has been enabled!");
			break;
		}
		
//		channelEnabled = getConfig().getBoolean("ChannelChatListener");
//		if(channelEnabled){
//			if(legitDefaultChannel(getConfig().getString("Default_Channel"))){
//				defaultChannel = getConfig().getString("Default_Channel");
//			}
//			else{
//				channelEnabled = false;
//			}
//
//		}
//		if(getConfig().getBoolean("JSON")){
//			JSON = true;
//		}
//		else{
//			JSON = false;
//		}
//		if(!getConfig().getBoolean("Custom_Chat_Enabled")){
//			getLogger().info("Custom chat is not enabled. The chat will not be modified!");
//		}
//		if(JSON){
//			Bukkit.getServer().getPluginManager().registerEvents(new Chat_Json(), this);
//			
//		}else if(channelEnabled){
//			Bukkit.getServer().getPluginManager().registerEvents(new ChannelChatListener(), this);
//		}else{
//			Bukkit.getServer().getPluginManager().registerEvents(new Normal(), this);
//		}
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
	public File getLangFile() {
	    return LANG_FILE;
	}
	public YamlConfiguration getLang() {
	    return LANG;
	}
	/**
	 * 
	 */
	@SuppressWarnings("static-access")
	public void loadLang() {
	    File lang = new File(getDataFolder(), "lang.yml");
	    if (!lang.exists()) {
	        try {
	            getDataFolder().mkdir();
	            lang.createNewFile();
	            InputStream defConfigStream = this.getResource("lang.yml");
	            if (defConfigStream != null) {
					YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream));
	                
	                defConfig.save(lang);
	                Lang.setFile(defConfig);
	                return;
	            }
	        } catch(IOException e) {
	            e.printStackTrace(); // So they notice
	            getLogger().severe("The language file could not be created. Disabling plugin.");
	            this.setEnabled(false);
	        }
	    }
	    YamlConfiguration conf = YamlConfiguration.loadConfiguration(lang);
	    for(Lang item:Lang.values()) {
	        if (conf.getString(item.getPath()) == null) {
	            conf.set(item.getPath(), item.getDefault());
	        }
	    }
	    Lang.setFile(conf);
	    this.LANG = conf;
	    this.LANG_FILE = lang;
	    try {
	        conf.save(getLangFile());
	    } catch(IOException e) {
	        getLogger().warning( "Failed to save lang.yml.");
	        e.printStackTrace();
	    }
	}
	
	
	
    
	
	
	private void registerConfig() {
		saveDefaultConfig();
	}
	
	/**
	 * Loads all of the Events and Commands.
	 */
	public void loadMethod(){
		getCommand("chat").setExecutor(new ChatCommand());
		getCommand("chat").setTabCompleter(new CommandTabCompleter());
		getCommand("sc").setExecutor(new StaffChat());
		getCommand("sctoggle").setExecutor(new StaffChatToggle());
		getCommand("spy").setExecutor(new SpyCommand());
		if(!(plugin.getConfig().getBoolean("ChatColor_Command")))
			getCommand("color").setExecutor(new ColorGUI());
		getCommand("channel").setExecutor(new ChannelCmd());
		Bukkit.getServer().getPluginManager().registerEvents(new StopChat(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new NoSwear(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new Spy(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new JoinListner(), this);
		//Bukkit.getServer().getPluginManager().registerEvents(new Format(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(new ColorGUI(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new Notify(), this);
		if(getConfig().getBoolean("console_log", true)) {
			Bukkit.getServer().getPluginManager().registerEvents(new ConsoleLogChat(this), this);
		}
		
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