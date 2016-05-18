package de.robingrether.idisguise.additions;

import java.io.File;
import java.lang.reflect.Field;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;

public class Configuration {
	
	public static final String DISABLE_ITEM_PICK_UP_PATH = "disable.item-pick-up";
	public static final String DISABLE_MOB_DAMAGE_PATH = "disable.mob-damage";
	public static final String DISABLE_MOB_TARGET_PATH = "disable.mob-target";
	public static final String UNDISGUISE_PVP_PATH = "undisguise.pvp";
	public static final String UNDISGUISE_PVE_PATH = "undisguise.pve";
	public static final String UNDISGUISE_PROJECTILE_PATH = "undisguise.projectile";
	
	public boolean DISABLE_ITEM_PICK_UP = false;
	public boolean DISABLE_MOB_DAMAGE = false;
	public boolean DISABLE_MOB_TARGET = false;
	public boolean UNDISGUISE_PVP = false;
	public boolean UNDISGUISE_PVE = false;
	public boolean UNDISGUISE_PROJECTILE = false;
	
	private Additions plugin;
	
	public Configuration(Additions plugin) {
		this.plugin = plugin;
	}
	
	public void loadData() {
		File configurationFile = new File(plugin.getDataFolder(), "config.yml");
		if(!configurationFile.exists()) {
			plugin.saveDefaultConfig();
		}
		plugin.reloadConfig();
		FileConfiguration fileConfiguration = plugin.getConfig();
		try {
			for(Field pathField : getClass().getDeclaredFields()) {
				if(pathField.getName().endsWith("_PATH")) {
					Field valueField = getClass().getDeclaredField(pathField.getName().substring(0, pathField.getName().length() - 5));
					if(fileConfiguration.isSet((String)pathField.get(null))) {
						if(fileConfiguration.isBoolean((String)pathField.get(null))) {
							valueField.setBoolean(this, fileConfiguration.getBoolean((String)pathField.get(null), valueField.getBoolean(this)));
						}
					}
				}
			}
		} catch(Exception e) {
			plugin.getLogger().log(Level.SEVERE, "An error occured while loading the config file.", e);
		}
	}
	
	public void saveData() {
		FileConfiguration fileConfiguration = plugin.getConfig();
		try {
			for(Field pathField : getClass().getDeclaredFields()) {
				if(pathField.getName().endsWith("_PATH")) {
					Field valueField = getClass().getDeclaredField(pathField.getName().substring(0, pathField.getName().length() - 5));
					if(valueField.getType() == boolean.class) {
						fileConfiguration.set((String)pathField.get(null), valueField.getBoolean(this));
					}
				}
			}
			plugin.saveConfig();
		} catch(Exception e) {
			plugin.getLogger().log(Level.SEVERE, "An error occured while saving the config file.", e);
		}
	}
	
}