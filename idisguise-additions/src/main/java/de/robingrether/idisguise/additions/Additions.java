package de.robingrether.idisguise.additions;

import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import org.bstats.Metrics;

public class Additions extends JavaPlugin {
	
	private EventListener listener;
	private Configuration configuration;
	private Language language;
	private Metrics metrics;
	
	public void onEnable() {
		checkDirectory();
		listener = new EventListener(this);
		configuration = new Configuration(this);
		configuration.loadData();
		configuration.saveData();
		language = new Language(this);
		language.loadData();
		language.saveData();
		metrics = new Metrics(this);
		getServer().getPluginManager().registerEvents(listener, this);
		getLogger().log(Level.INFO, String.format("%s enabled!", getFullName()));
	}
	
	public void onDisable() {
		getLogger().log(Level.INFO, String.format("%s disabled!", getFullName()));;
	}
	
	public void onReload() {
		configuration.loadData();
		configuration.saveData();
		language.loadData();
		language.saveData();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("idisguiseadditions")) {
			if(sender instanceof ConsoleCommandSender || (sender instanceof Player && ((Player)sender).hasPermission("iDisguiseAdditions.reload"))) {
				onReload();
				sender.sendMessage(ChatColor.GOLD + "Reloaded config file.");
			} else {
				sender.sendMessage(ChatColor.RED + "You are not allowed to do this.");
			}
		}
		return true;
	}
	
	private void checkDirectory() {
		if(!getDataFolder().exists()) {
			getDataFolder().mkdir();
		}
	}
	
	public String getVersion() {
		return getDescription().getVersion();
	}
	
	public String getFullName() {
		return "iDisguiseAdditions " + getVersion();
	}
	
	public Configuration getConfiguration() {
		return configuration;
	}
	
	public Language getLanguage() {
		return language;
	}
	
}