package com.fvza.rankup.bukkit;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.fvza.rankup.util.FileManager;
import com.fvza.rankup.util.Language;

public class ListenerSign implements Listener {

	@EventHandler
	public void onSignChange( SignChangeEvent event ) {
        Player p = event.getPlayer();
        
        FileManager FileManager = new FileManager();
        
        if(event.getLine(0).equalsIgnoreCase("[rankup]")){
        	
        	if( p.hasPermission("rankup.sign") ){
        		
        		YamlConfiguration data = FileManager.getDataFile();
        		System.out.println(data);
        		ConfigurationSection section = data.getConfigurationSection("data");
        		
        		if( section.getList("data.signs") != null ){
        			section.set("data.signs", section.getStringList("data.signs").add( event.getBlock().getLocation().toString() ) );
        		} else {
        			List<String> list = new ArrayList<String>();
        			section.set("data.signs", list.add( event.getBlock().getLocation().toString() ) );
        		}
        		
        		event.setLine(0, ChatColor.DARK_BLUE + "[Rankup]");
        		event.setLine(1, "Right click me");
        		event.setLine(2, "to rank up!");
        		
            	Language.send(p, "&7Successfully created a rank up sign." );
            	
            	
        	} else {
        		return; 
        	}
        	
        }
    }
	
	@EventHandler
	public static void onInteract( PlayerInteractEvent event ) {
		if(event.getAction().name() != "RIGHT_CLICK_BLOCK"){
			return; 
		}
		
		if(event.getClickedBlock().getTypeId() != 323){
			return; 
		}
		
	}
	
	@EventHandler
	public static void onBlockBreak( BlockBreakEvent event ) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		
		if( block.getTypeId() != 323 ){
			return; 
		}
		
		if( !player.hasPermission( "rankup.sign" ) ){
			Language.send( player, "&7You do not have permission to break this sign." );
			event.setCancelled( true );
		} else {
			// TODO: Check if it's in database
			Language.send( player, "&7You have succesfully removed this rank up sign." );
			// TODO: Remove from database 
		}
	}
}
