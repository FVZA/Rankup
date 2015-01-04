package org.cyberiantiger.minecraft.rankup;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RankupCommand implements CommandExecutor {
    private final Main plugin;
    
    public RankupCommand(Main plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand( CommandSender sender, Command command, String label, String[] args ) {
        if( !(sender instanceof Player) ){
            return false;
        }
        
        Player player = (Player) sender;
        
        if( command.getName().equalsIgnoreCase("rankup") ) {
            if(args.length == 0){
                plugin.rankup(player);
            } else if ( args.length == 1 ){
                if( args[0].equalsIgnoreCase("v") || args[0].equalsIgnoreCase("version") ){
                    if( player.hasPermission(Main.PERMISSION_VERSION)){
                        player.sendMessage(plugin.translate("version.success", plugin.getDescription().getVersion()));
                    } else {
                        player.sendMessage(plugin.translate("version.no-permission"));
                    }
                } else if ( args[0].equalsIgnoreCase("r") || args[0].equalsIgnoreCase("reload") ){
                    if( player.hasPermission("rankup.reload")){
                        if (plugin.reloadRanks())  {
                            player.sendMessage(plugin.translate("reload.ranks.success"));
                        } else {
                            player.sendMessage(plugin.translate("reload.ranks.fail"));
                        }
                        if (plugin.reloadTranslations()) {
                            player.sendMessage(plugin.translate("reload.translation.success"));
                        } else {
                            player.sendMessage(plugin.translate("reload.translation.fail"));
                        }
                    } else {
                        player.sendMessage(plugin.translate("reload.no-permission"));
                    }
                }
            }
        }
        return true;
    }
}
