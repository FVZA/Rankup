package com.fvza.rankup.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class FileManager {
	private File configFile; 
	private File langFile; 
	
	private YamlConfiguration config;
	private YamlConfiguration lang; 
    
	public void loadFiles(){
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Rankup");
				
		configFile = new File( plugin.getDataFolder(), "config.yml" );
		langFile = new File( plugin.getDataFolder(), "language.yml" );
		
		if(!configFile.exists()){
			try {
				plugin.saveDefaultConfig();
				System.out.println("[Rankup] No config found, generating...");
			} catch (Exception ex) {
				throw new IllegalArgumentException("Unable to create the config file.");
			}
		}
		
		
		if(!langFile.exists()){
			try {
				InputStream jarURL = Config.class.getResourceAsStream("/language.yml");
				copyFile(jarURL, langFile);
				System.out.println("[Rankup] No language file found, generating...");
			} catch (Exception ex) {
				throw new IllegalArgumentException("Unable to create the language file.");
			}
		}
		
		config = new YamlConfiguration();
		lang = new YamlConfiguration();
		
		try {
			config.load(configFile);
			lang.load(langFile);
		} catch (Exception ex) {
			throw new IllegalArgumentException("Unable to load a configuration file. Make sure you did not use the tab key in your config.");
		}
		
		Config Config = new Config(); 
		Config.loadConfig( config ); 
		Language.load( lang );
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
