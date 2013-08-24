package com.fvza.rankup;

import java.io.IOException;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.fvza.rankup.util.Config;

public class Rankup extends JavaPlugin{
	
	public static Permission perms = null;
	public static Economy econ = null;
	
	private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }
	
	private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null; 
    }
	
	@Override
    public void onEnable() {
		
		Config Config = new Config();
		
		PluginDescriptionFile pdfFile = this.getDescription();	
		
		getCommand("rankup").setExecutor(new Commands());
		
		setupPermissions();
		setupEconomy();
		
		Config.loadConfig();
		
		getServer().getPluginManager().registerEvents(new ListenerSign(), this);
		
		try {
		    Metrics metrics = new Metrics(this);
		    metrics.start();
		} catch (IOException e) {
			getLogger().info( "Unable to send Metrics data." );
		}
		 
		getLogger().info( pdfFile.getName() + " " + pdfFile.getVersion() + " is now enabled." );
	}
	
	@Override
	public void onDisable() {
		PluginDescriptionFile pdfFile = this.getDescription();	
		
		getLogger().info( pdfFile.getName() + " " + pdfFile.getVersion() + " is now disabled." );
	}
}
