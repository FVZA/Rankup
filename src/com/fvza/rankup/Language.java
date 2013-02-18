package com.fvza.rankup;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Language {

	public static boolean send ( Player player, String msg ){
		
		if( !(player instanceof Player) || player == null || !player.isOnline()) {
			return false; 
		} else {
			player.sendMessage( ChatColor.translateAlternateColorCodes('&', msg) );
			return true;
		}
	}
	
	public static boolean broadcast (String msg ){
		Bukkit.getServer().broadcastMessage( ChatColor.translateAlternateColorCodes('&', msg) );
		return true;
	}
		
}
