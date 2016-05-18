package de.robingrether.idisguise.additions;

import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import de.robingrether.idisguise.io.Metrics;

public class Additions extends JavaPlugin {
	
	private EventListener listener;
	private Configuration configuration;
	private Metrics metrics;
	
	public void onEnable() {
		checkDirectory();
		listener = new EventListener(this);
		configuration = new Configuration(this);
		configuration.loadData();
		try {
			metrics = new Metrics(this);
			metrics.start();
		} catch(Exception e) {
		}
		getServer().getPluginManager().registerEvents(listener, this);
		getLogger().log(Level.INFO, String.format("%s enabled!", getFullName()));
	}
	
	public void onDisable() {
		getLogger().log(Level.INFO, String.format("%s disabled!", getFullName()));;
	}
	
	public void onReload() {
		configuration.loadData();
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
	
}