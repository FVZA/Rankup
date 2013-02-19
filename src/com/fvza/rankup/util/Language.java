package com.fvza.rankup.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Language {

	public static void send ( Player player, String msg ){
		
		if( !( player instanceof Player ) || player == null || !player.isOnline() ) {
			return; 
		} else {
			player.sendMessage( ChatColor.translateAlternateColorCodes('&', msg) );
			return;
		}
	}
	
	public static void broadcast (String msg ){
		Bukkit.getServer().broadcastMessage( ChatColor.translateAlternateColorCodes('&', msg) );
	}
		
}
