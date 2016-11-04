package de.robingrether.idisguise.additions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;

import de.robingrether.util.StringUtil;

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
		File configurationFile = new File(plugin.getDataFolder(), "config.yml");
		String config = StringUtil.readFrom(plugin.getResource("config.yml"));
		try {
			for(Field pathField : getClass().getDeclaredFields()) {
				if(pathField.getName().endsWith("_PATH")) {
					Field valueField = getClass().getDeclaredField(pathField.getName().substring(0, pathField.getName().length() - 5));
					config = config.replace(valueField.getName(), valueField.get(this).toString());
				}
			}
			OutputStream output = new FileOutputStream(configurationFile);
			output.write(config.getBytes());
			output.close();
		} catch(Exception e) {
			plugin.getLogger().log(Level.SEVERE, "An error occured while saving the config file.", e);
		}
	}
	
}