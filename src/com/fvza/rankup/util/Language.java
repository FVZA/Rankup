package com.fvza.rankup.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class Language {
	
	static String player;
	static String broadcast;

	static String noMoney;
	static String notRankable;
	static String noNextGroup;
	
	/*
	 * rankupSuccess,rank
	 * noMoney,cost,rank
	 * noNextGroup
	 * notRankable
	 */
	
	public static void load ( YamlConfiguration config ){
		player = config.getString("rankup-success-player");
		broadcast = config.getString("rankup-success-broadcast");
		
		noMoney = config.getString("rankup-failed-money");
		notRankable = config.getString("rankup-failed-next-group-not-allowed");
		noNextGroup = config.getString("rankup-failed-no-group-to-rank-up-to");

	}

	public static void send ( Player player, String msg ){
		
		if( !( player instanceof Player ) || player == null || !player.isOnline() ) {
			return; 
		} else {
			if( msg.contains("rankupSuccess")){
				//player.sendMessage( ChatColor.translateAlternateColorCodes("&", msg.replace("%RANK%", msg.concat(",")[1].toString() ) ) ); 
			}
				player.sendMessage( ChatColor.translateAlternateColorCodes('&', msg) );
			return;
		}
	}
	
	public static void broadcast (String msg ){
		Bukkit.getServer().broadcastMessage( ChatColor.translateAlternateColorCodes('&', msg) );
	}
		
}
