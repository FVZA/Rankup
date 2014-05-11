package org.cyberiantiger.minecraft.rankup;

import java.io.IOException;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;


import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.IllegalFormatException;
import java.util.Map;
import java.util.logging.Level;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.entity.Player;

public class Main extends JavaPlugin {

    public static final String PERMISSION_SIGN = "rankup.sign";
    public static final String PERMISSION_RANKUP = "rankup.rankup";
    public static final String PERMISSION_RELOAD = "rankup.reload";
    public static final String PERMISSION_VERSION = "rankup.version";

    private Permission perms;
    private Economy econ;
    private Ranks ranks;
    private Map<String, String> translations;
    
    private void setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        if (perms == null) {
            throw new IllegalStateException("No vault compatable permission plugins found.");
        }
        if (!perms.hasGroupSupport()) {
            throw new IllegalStateException("Your permissions plugin does not have group support.");
        }
        if (!perms.hasSuperPermsCompat()) {
            throw new IllegalStateException("Your permissions plugin does not have super perms compatability.");
        }
    }
    
    private void setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        econ = rsp.getProvider();
        if (econ == null) {
            throw new IllegalStateException("No vault compatable economy plugins found.");
        }
    }

    @Override
    public void onEnable() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            getLogger().severe("Disabling plugin, Vault not found");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getCommand("rankup").setExecutor(new RankupCommand(this));
        
        getServer().getPluginManager().registerEvents(new SignListener(this), this);
        
        if (!reloadRanks()) {
            getLogger().log(Level.SEVERE, "Disabling plugin, invalid configuration.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if (!reloadTranslations()) {
            getLogger().log(Level.SEVERE, "Disabling plugin, invalid configuration.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        // Delay economy and permissions init until after all
        // plugins are loaded.
        getServer().getScheduler().runTask(this, new Runnable() {
            @Override
            public void run() {
                try {
                    setupPermissions();
                } catch (IllegalStateException e) {
                    getLogger().severe(e.getMessage());
                    getPluginLoader().disablePlugin(Main.this);
                    return;
                }
                try {
                    setupEconomy();
                } catch (IllegalStateException e) {
                    getLogger().severe(e.getMessage());
                    getPluginLoader().disablePlugin(Main.this);
                }
            }
        });
    }
    
    @Override
    public void onDisable() {
    }

    public String translate(String messageName, Object... arguments) {
        String format = translations.get(messageName);
        String result;
        if (format != null) {
            try {
                result = String.format(format, arguments);
            } catch (IllegalFormatException e) {
                result = String.format("Invalid format message: %s format: %s arguments: %s", messageName, format, Arrays.asList(arguments));
                getLogger().log(Level.WARNING, result, e);
            }
        } else {
            result = String.format("Unknown message: %s arguments: %s", messageName, Arrays.asList(arguments));
            getLogger().warning(result);
        }
        return result;
    }

    public boolean rankup(Player p) {
        if (!p.hasPermission(PERMISSION_RANKUP)) {
            p.sendMessage(translate("rankup.no-permission"));
            return false;
        }
        Rank current = ranks.getCurrentRank(perms, p);
        if (current == null) {
            p.sendMessage(translate("rankup.no-current-rank"));
            return false;
        }
        Rank next = ranks.getNextRank(current);
        if (next == null) {
            p.sendMessage(translate("rankup.no-next-rank", current.getName()));
            return false;
        }
        double price = next.getPrice();

        EconomyResponse transaction = econ.withdrawPlayer(p.getName(), p.getWorld().getName(), price);

        if (!transaction.transactionSuccess()) {
            p.sendMessage(translate("rankup.no-money", current.getName(), next.getName(), price));
            return false;
        }

        for (GroupAssignment assignment : current.getGroups()) {
            perms.playerRemoveGroup(assignment.getWorld(), p.getName(), assignment.getGroup());
        }

        for (GroupAssignment assignment : next.getGroups()) {
            perms.playerAddGroup(assignment.getWorld(), p.getName(), assignment.getGroup());
        }

        p.sendMessage(translate("rankup.success", current.getName(), next.getName(), price));

        getServer().broadcastMessage(translate("rankup.success-broadcast", p.getName(), p.getDisplayName(), current.getName(), next.getName(), price));
            
        return true;
    }

    public boolean reloadRanks() {
        try {
            Ranks ranks = ConfigLoader.loadRanks(this);
            ranks.init();
            this.ranks = ranks;
            getLogger().info("Loaded ranks: " + ranks.toString());
            return true;
        } catch (IOException ex) {
            getLogger().log(Level.WARNING, "Error reading ranks configuration.", ex);
            return false;
        } catch (IllegalStateException ex) {
            getLogger().log(Level.WARNING, "Error reloading ranks configuration.", ex);
            return false;
        }
    }

    public boolean reloadTranslations() {
        try {
            translations = ConfigLoader.loadTranslations(this);
            return true;
        } catch (IOException ex) {
            getLogger().log(Level.WARNING, "Error reading translation configuration.", ex);
            return false;
        }
    }
}