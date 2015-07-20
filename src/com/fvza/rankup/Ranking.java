package com.fvza.rankup;

import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.entity.Player;

import com.fvza.rankup.util.Config;
import com.fvza.rankup.util.Language;

public class Ranking {
	
	public static boolean pay(Player player, Double amount){
		
		String newRank = Config.getRankToGroup( player );
		
		if ( Rankup.econ == null ){
			
			Language.send( player, "&cNo valid economy plugin found. Tell an administrator.");
			return false; 
			
		}
		
		EconomyResponse r = Rankup.econ.withdrawPlayer(player.getName(), amount );
		
		if(r.transactionSuccess()){
			return true; 
		} else {
			Language.noMoney( player, amount, newRank );
			return false; 
		}
	}
	
	public static boolean rankup(Player player){
		
		if( Rankup.perms.getGroups().length == 0 || !Rankup.perms.hasSuperPermsCompat() ){
			
			Language.send( player, "&cNo valid permissions plugin found. Tell an administrator.");
			return false; 
			
		}
		
		if( !player.hasPermission("rankup.rankup")){
			Language.send( player, "&cYou do not have permission to rankup.");
		}
		
		if( Config.getRankToGroup( player ) != null ){
			
			String newRank = Config.getRankToGroup( player );
			Double rankPrice = Config.getGroupPrice( newRank );
			
			if( rankPrice < 0 ){
				Language.send( player, "notRankable");
				return false; 
			}
			
			
			boolean paid = pay( player, rankPrice );  
			
			if( paid ){
				
				if( Config.getOverride() ){

					for(String b : Rankup.perms.getPlayerGroups( player )){
						
						if(b != newRank){
							
							Rankup.perms.playerRemoveGroup(player, b);
							
						}
					}
						
				} else {
					Rankup.perms.playerRemoveGroup(player, Config.getCurrentRankableGroup( player ));
				}
			
				Rankup.perms.playerAddGroup(player, newRank);
				Language.broadcast( "&b" + player.getDisplayName() + "&3 has ranked up to &b" + newRank + "." );
				
				return true;
				
			}
		}
		
		return false; 
	}
}
