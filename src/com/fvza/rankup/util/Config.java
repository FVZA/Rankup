package com.fvza.rankup.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.fvza.rankup.Rankup;

public class Config {
	
	private static YamlConfiguration config;
	private static YamlConfiguration data;
	
	private static File configFile;
	private static File dataFile;
	
	public static boolean override;
	public static String[] groupNames; 
	public static Double[] groupPrices; 
	public static int[] groupTimes;
	
	public static ArrayList<String> getAvailableGroups(){
		return new ArrayList<String>( Arrays.asList( groupNames ) );
	}
	
	
	public static ArrayList<String> getCurrentGroups( Player player ){
		return new ArrayList<String>( Arrays.asList( Rankup.perms.getPlayerGroups( player ) ) );
	}
	
	public static Double[] getGroupPrices(){
		return groupPrices; 
	}
	
	public static boolean getOverride(){
		return override; 
	}
	
	public static String getCurrentRankableGroup( Player player ){	
		ArrayList<String> availableGroups = getAvailableGroups();
		ArrayList<String> currentGroups = getCurrentGroups( player );
		
		if( availableGroups.size() == 0 || availableGroups == null ){
			Language.send( player, "&cThere are no available groups to rank up to." );			
			return null;
		}
		
		if( currentGroups.size() == 0 || currentGroups == null ){
			Language.send( player, "&cYou are not in a group that can rank up." );
			return null; 
		}
		
		int count = 0; 
		
		for( String s : availableGroups ){
			
			if( count == availableGroups.size() ){
				Language.send( player, "&cYou are not in a group that can rank up." );
			} else if( currentGroups.contains(s) ){	
				return availableGroups.get( count ) ; 	
			}
			 
			count++;
		}		
		
		return null; 
		
	}
	
	public static String getRankToGroup( Player player ){
		ArrayList<String> availableGroups = getAvailableGroups();
		ArrayList<String> currentGroups = getCurrentGroups( player );
		
		if( availableGroups.size() == 0 || availableGroups == null ){
			Language.send( player, "&cThere are no available groups to rank up to." );
			return null;
		}
		
		if( currentGroups.size() == 0 || currentGroups == null ){
			Language.send( player, "&cYou are not in a group that can rank up." );
			return null; 
		}
		
		int count = 0; 
		
		for( String s : availableGroups ){
			
			if( count == availableGroups.size()-1 ){
				Language.send( player, "&cYou are not in a group that can rank up." );
			} else if( currentGroups.contains(s) && availableGroups.get(count + 1) != null ){		
				return availableGroups.get( count + 1 ) ; 	
			}
			 
			count++;
		}		
		
		return null; 
		
	}
	
	public static Double getGroupPrice( String rank ){
		int groupCount = 0; 
		
		for(@SuppressWarnings("unused") String c : groupNames){
			if (groupNames[groupCount] == rank){
				
				return groupPrices[groupCount];	
			}
			
			groupCount++; 
		}
		
		return null;
		
	}
	
	public void loadConfig(){
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Rankup");
		
		configFile = new File(plugin.getDataFolder(), "config.yml");
		
		if(!configFile.exists()){
			plugin.saveDefaultConfig();
		} 
		
		config = new YamlConfiguration();
		
		try {
			config.load(configFile);
		} catch (Exception ex) {
			throw new IllegalArgumentException("Your configuration file is invalid. Make sure you are not using the tab key, as YML does not understand it.");
		}
		
		Map<String, Object> allGroups = new HashMap<String, Object>();
		
		if(!config.getKeys(false).isEmpty()){
		
			allGroups = (Map<String, Object>)config.getConfigurationSection("ranks").getValues(false);
		
			if (allGroups != null) {

				Iterator<String> groupItr = allGroups.keySet().iterator();
				
				groupNames = new String[allGroups.size()];
				groupPrices = new Double[allGroups.size()];
				
				String groupName;
				Integer groupCount = 0;
			
				while (groupItr.hasNext()) {
					try {
						groupCount++;
						groupName = groupItr.next();
					}
					catch (Exception ex) {
						throw new IllegalArgumentException("Invalid group name found, check your configuration file.");
					}
					
					Double number = new Double(config.getDouble("ranks." + groupName));
					
					groupNames[groupCount-1] = groupName;
					groupPrices[groupCount-1] = number;
			
				}
			
			}
		}
		
		override = config.getBoolean("settings.override-groups");

    }
	
	public void loadData(){
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Rankup");
		
		dataFile = new File(plugin.getDataFolder(), "data.yml");
		
		if(!dataFile.exists()){
			
		} 
		
		data = new YamlConfiguration();
		
		try {
			data.load( dataFile );
		} catch (Exception ex) {
			throw new IllegalArgumentException("Unable to load the data file.");
		}
		
	}
	

}
