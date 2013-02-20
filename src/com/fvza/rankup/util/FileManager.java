package com.fvza.rankup.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class FileManager {
   // private static File configFile;
   // private static File dataFile;
	private File configFile; 
	private File dataFile; 
	
	private YamlConfiguration config;
	private YamlConfiguration data;
    
	public void loadFiles(){
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Rankup");
				
		configFile = new File( plugin.getDataFolder(), "config.yml" );
		dataFile = new File( plugin.getDataFolder(), "data.yml" );
		
		if(!configFile.exists()){
			plugin.saveDefaultConfig();
		}
		
		if (!dataFile.exists()){
			try {
				InputStream jarURL = Config.class.getResourceAsStream("/data.yml");
				copyFile(jarURL, dataFile);
			} catch (Exception ex) {
				throw new IllegalArgumentException("Unable to create the data file.");
			}
		}
		
		config = new YamlConfiguration();
		data = new YamlConfiguration();
		
		try {
			config.load(configFile);
			data.load(dataFile);
		} catch (Exception ex) {
			throw new IllegalArgumentException("Your configuration and or data file is invalid. Make sure you are not using the tab key, as YML does not understand it.");
		}
		
		Config Config = new Config(); 
		Config.loadConfig( config ); 
	}
	
	public YamlConfiguration getConfigFile(){
		return config; 
	}
	
	public YamlConfiguration getDataFile(){
		return data;
	}
	
	private void copyFile(InputStream in, File file) {
        try {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
