package com.fvza.rankup;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ListenerSign implements Listener {

	@EventHandler
	public void onSignChange(SignChangeEvent event) {
        Player p = event.getPlayer();
        
        if(event.getLine(0).equalsIgnoreCase("[rankup]")){
        	       	
        	if( p.hasPermission("rankup.sign") ){
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
	public static void onInteract(PlayerInteractEvent event) {
		if(event.getAction().name() != "RIGHT_CLICK_BLOCK"){
			return; 
		}
		
		if(event.getClickedBlock().getTypeId() == 332){
			//if(event.getClickedBlock().getLine(1)){
				
			//}
		}
	}
}
