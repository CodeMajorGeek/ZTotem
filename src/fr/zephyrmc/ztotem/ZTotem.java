package fr.zephyrmc.ztotem;

import java.io.*;

import org.bukkit.plugin.java.*;

import fr.zephyrmc.ztotem.commands.*;

public class ZTotem extends JavaPlugin {
	
	public File configFile;
	
	public static ZTotem mainPlugin;
	
	@Override
	public void onEnable() {
		
		mainPlugin = this;
		getDataFolder().mkdirs();
		configFile = new File(getDataFolder(), "config.yml");
		try {
			
			configFile.createNewFile();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		if(!getConfig().contains("allEvents")) {
			
			getConfig().createSection("allEvents");
			saveConfig();
		}
		
		getCommand("totem").setExecutor(new TotemCommand());
	}

	@Override
	public void onDisable() {
		
		
	}
	
	public void saveConfig() {
		
		try {
			
			getConfig().save(configFile);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
}
