package com.fvza.rankup.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class Language {
	
	static String broadcast;

	static String noMoney;
	static String notRankable;
	
	/*
	 * rankupSuccess,rank
	 * noMoney,cost,rank
	 * noNextGroup
	 * notRankable
	 */
	
	public static void load ( YamlConfiguration config ){
		broadcast = config.getString("rankup-success-broadcast");
		
		noMoney = config.getString("rankup-failed-money");
		notRankable = config.getString("rankup-failed-next-group-not-allowed");

	}

	public static void send ( Player player, String msg ){
		
		if( !( player instanceof Player ) || player == null || !player.isOnline() ) {
			return; 
		} else {
			if( msg.contains("rankupSuccess")){
				//player.sendMessage( ChatColor.translateAlternateColorCodes("&", msg.replace("%RANK%", msg.concat(",")[1].toString() ) ) ); 
			} else if ( msg.contains("notRankable")){
				player.sendMessage( ChatColor.translateAlternateColorCodes('&', notRankable) );
			}
			return;
		}
	}
	
	public static void noMoney( Player player, Double amount, String rank ){
		String msg =  noMoney.replace("%COST%", amount.toString());
		String msg2 = msg.replace("%RANK%", rank );
		
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg2));
	}
	
	public static void broadcast (String msg ){
		Bukkit.getServer().broadcastMessage( ChatColor.translateAlternateColorCodes('&', msg) );
	}
		
}
