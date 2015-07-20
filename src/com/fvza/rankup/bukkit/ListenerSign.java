package com.fvza.rankup.bukkit;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.fvza.rankup.Ranking;
import com.fvza.rankup.util.Language;



public class ListenerSign implements Listener {

	
	@EventHandler
	public void onBlockBreak( BlockBreakEvent event ) {
		Player player = event.getPlayer();
		Block block = event.getBlock();
		
		if( block.getType() == Material.SIGN_POST || block.getType() == Material.WALL_SIGN ){
			Sign s = (Sign)block.getState();
			
			if(s.getLine(0).equalsIgnoreCase("§1[Rankup]")){
				if( !player.hasPermission( "rankup.sign" ) ){
					Language.send( player, "&cYou do not have permission to break this sign." );
					event.setCancelled( true );
				} else {
					Language.send( player, "&7You have successfully removed this rankup sign." );
				}
			}
			
		}
		
	}


@EventHandler
public void onSignChange(SignChangeEvent e) {
  Player p = e.getPlayer();
 
  if (e.getLine(0).equalsIgnoreCase("[Rankup]"))
    
	  if (p.hasPermission("rankup.sign")) {
     
    	if (e.getLine(1).isEmpty() && e.getLine(2).isEmpty() && e.getLine(3).isEmpty() ) {
    		e.setLine(0, ChatColor.DARK_BLUE + "[Rankup]");
    		e.setLine(1, "Right click me");
    		e.setLine(2, "to rank up!");
  		
    		Language.send(p, "&7Successfully created a rankup sign." );

      }

    } else {
      Language.send(p, "&cYou do not have permission to create a rankup sign.");
      e.setCancelled(true);
    }
}


	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			
			Block b = event.getClickedBlock();
			
			if ((b.getType() == Material.SIGN_POST) || (b.getType() == Material.WALL_SIGN)) {
				
				Sign s = (Sign)b.getState();
				Player p = event.getPlayer();
					
				if (s.getLine(0).equalsIgnoreCase("§1[Rankup]"))
						Ranking.rankup( p );
				}
		}
  	}

}


