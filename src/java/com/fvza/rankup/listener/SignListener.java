package com.fvza.rankup.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.fvza.rankup.Rankup;


public class SignListener implements Listener {

    private final Rankup plugin;

    public SignListener(Rankup plugin) {
        this.plugin = plugin;
    }
    
    
    @EventHandler
    public void onBlockBreak( BlockBreakEvent event ) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        
        if( block.getType() == Material.SIGN_POST || block.getType() == Material.WALL_SIGN ){
            Sign s = (Sign)block.getState();
            
            if(s.getLine(0).equals(plugin.translate("sign.text.line1"))){
                if( !player.hasPermission(Rankup.PERMISSION_SIGN) ){
                    player.sendMessage(plugin.translate("sign.break.fail"));
                    event.setCancelled( true );
                } else {
                    player.sendMessage(plugin.translate("sign.break.success"));
                }
            }
            
        }
        
    }
    
    
    @EventHandler
    public void onSignChange(SignChangeEvent e) {
        Player p = e.getPlayer();
        if (e.getLine(0).equalsIgnoreCase(plugin.translate("sign.create.line1"))) {
            if (p.hasPermission(Rankup.PERMISSION_SIGN)) {
                e.setLine(0, plugin.translate("sign.text.line1"));
                e.setLine(1, plugin.translate("sign.text.line2"));
                e.setLine(2, plugin.translate("sign.text.line3"));
                e.setLine(3, plugin.translate("sign.text.line4"));
                p.sendMessage(plugin.translate("sign.create.success"));
                e.setCancelled(true);
            } else {
                p.sendMessage(plugin.translate("sign.create.fail"));
            }
        }
    }
    
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            
            Block b = event.getClickedBlock();
            
            if ((b.getType() == Material.SIGN_POST) || (b.getType() == Material.WALL_SIGN)) {
                
                Sign s = (Sign)b.getState();
                Player p = event.getPlayer();
                
                if (s.getLine(0).equals(plugin.translate("sign.text.line1"))) {
                    plugin.rankup( p );
                }
            }
        }
    }
    
}


