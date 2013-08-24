package com.fvza.rankup.bukkit;

import org.bukkit.event.Listener;



public class ListenerSign implements Listener {
/*
 @EventHandler
	public void onSignChange( SignChangeEvent event ) {
        Player p = event.getPlayer();
        
        FileManager FileManager = new FileManager();
        
        if(event.getLine(0).equalsIgnoreCase("[rankup]")){
        	
        	if( p.hasPermission("rankup.sign") ){
        		
        		event.setLine(0, ChatColor.DARK_BLUE + "[Rankup]");
        		event.setLine(1, "Right click me");
        		event.setLine(2, "to rank up!");
        		
            	Language.send(p, "&7Successfully created a rank up sign." );
            	
            	
        	} else {
        		event.setLine(0, "");
        		
        		Language.send(p, "&cYou do not have permission to create a rankup sign.");
        	}
        	
        }
    }
	
	@EventHandler
	public void onInteract( PlayerInteractEvent event ) {
		if(event.getAction().name() != "RIGHT_CLICK_BLOCK"){
            if (event.getClickedBlock().getState() instanceof Sign) {
                
            	 
                Sign s = (Sign) event.getClickedBlock().getState();
             
                if(s.getLine(0).equalsIgnoreCase("[Rankup]")){
                    
                	//Ranking.rankup(event.getPlayer());
                }
            }
		}
		
		if(event.getClickedBlock().getTypeId() != 323){
			return; 
		}
		
	}
	
	@EventHandler
	public void onBlockBreak( BlockBreakEvent event ) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		
		if( block.getState() instanceof Sign){
			Sign s = (Sign) event.getBlock().getState();
			//if( Sign.getLine(0) == "[Rankup]"){
				if( !player.hasPermission( "rankup.sign" ) ){
					Language.send( player, "&cYou do not have permission to break this sign." );
					event.setCancelled( true );
				} else {
					Language.send( player, "&7You have succesfully removed this rank up sign." );
				}
			//}
		}
		
	}*/
}
