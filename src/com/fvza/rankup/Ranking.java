package com.fvza.rankup;

import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.entity.Player;

public class Ranking {
	
	public boolean pay(Player player, Double amount){
		
		if ( Rankup.econ == null ){
			
			Language.send( player, "&cNo valid economy plugin found. Tell an administrator.");
			return false; 
			
		}
		
		EconomyResponse r = Rankup.econ.withdrawPlayer(player.getName(), amount );
		
		if(r.transactionSuccess()){
			return true; 
		} else {
			player.sendMessage(String.format("%s", r.errorMessage));
			return false; 
		}
	}
	
	public boolean rankup(Player player){
		
		if( Rankup.perms.getGroups().length == 0 || !Rankup.perms.hasSuperPermsCompat() ){
			
			Language.send( player, "&cNo valid permissions plugin found. Tell an administrator.");
			return false; 
			
		}
		
		if( Config.getRankToGroup( player ) != null ){
			
			String newRank = Config.getRankToGroup( player );
			Double rankPrice = Config.getGroupPrice( newRank );
			
			if( rankPrice < 0 ){
				Language.send( player, "&cYou are not allowed to rank up to " + newRank + ".");
				return false; 
			}
			
			boolean paid = pay( player, rankPrice );  
			
			if( paid ){
				
				if( Config.getOverride() ){

					for(String b : Rankup.perms.getPlayerGroups( player )){
						Rankup.perms.playerRemoveGroup(player, b);
					}
						
				} else {
					Rankup.perms.playerRemoveGroup(player, Config.getCurrentRankableGroup( player ));
					System.out.println(Config.getAvailableGroups());
				}
        	
				Rankup.perms.playerAddGroup(player, newRank);
			
				
				Language.send( player, "&3You have successfully ranked up to &a" + newRank + "." );
				Language.broadcast( "&b" + player.getDisplayName() + "&3 has ranked up to &b" + newRank + "." );
				
				return true;
				
			}
		}
		
		return false; 
	}
}
