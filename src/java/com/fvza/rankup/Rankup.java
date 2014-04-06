package com.fvza.rankup;

import com.fvza.rankup.config.ConfigLoader;
import com.fvza.rankup.config.GroupAssignment;
import com.fvza.rankup.config.Rank;
import java.io.IOException;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;


import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.fvza.rankup.listener.SignListener;
import java.util.Arrays;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.entity.Player;

public class Rankup extends JavaPlugin {

    public static final String PERMISSION_SIGN = "rankup.sign";
    public static final String PERMISSION_RANKUP = "rankup.rankup";
    public static final String PERMISSION_RELOAD = "rankup.reload";
    public static final String PERMISSION_VERSION = "rankup.version";

    private Permission perms;
    private Economy econ;
    private List<Rank> ranks;
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
            throw new IllegalStateException("Missing dependency: Vault");
        }

        getCommand("rankup").setExecutor(new RankupCommand(this));
        
        getServer().getPluginManager().registerEvents(new SignListener(this), this);
        
        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (IOException e) {
            getLogger().info( "Unable to send Metrics data." );
        }

        saveDefaultConfig();

        load();

        // Delay economy and permissions init until after all
        // plugins are loaded.
        getServer().getScheduler().runTask(this, new Runnable() {
            @Override
            public void run() {
                try {
                    setupPermissions();
                } catch (IllegalStateException e) {
                    getLogger().severe(e.getMessage());
                    getPluginLoader().disablePlugin(Rankup.this);
                }
                try {
                    setupEconomy();
                } catch (IllegalStateException e) {
                    getLogger().severe(e.getMessage());
                    getPluginLoader().disablePlugin(Rankup.this);
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

    private Rank getNextRank(Rank current) {
        int i = ranks.indexOf(current);
        if (i == -1) 
            return null;
        i++;
        if (i >= ranks.size())
            return null;
        return ranks.get(i);
    }

    private Rank getCurrentRank(Player p) {
        for (int i = ranks.size() - 1; i>= 0; i--) {
            Rank testRank = ranks.get(i);
            boolean member = true;
            for (GroupAssignment group : testRank.getGroups()) {
                if (!perms.playerInGroup(group.getWorld(), p.getName(), group.getGroup())) {
                    member = false;
                    break;
                }
            }
            if (member) {
                return testRank;
            }
        }
        return null;
    }

    public boolean rankup(Player p) {
        if (!p.hasPermission(PERMISSION_RANKUP)) {
            p.sendMessage(translate("rankup.no-permission"));
            return false;
        }
        Rank current = getCurrentRank(p);
        if (current == null) {
            p.sendMessage(translate("rankup.no-current-rank"));
            return false;
        }
        Rank next = getNextRank(current);
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

        getServer().broadcastMessage(translate("rankup.success-broadcast", p.getDisplayName(), current.getName(), next.getName(), price));
            
        return true;
    }

    public void load() {
        ranks = ConfigLoader.loadRanks(getConfig());
        translations = ConfigLoader.loadTranslations(getConfig());
    }

    public void reload() {
        reloadConfig();
        load();
    }
}