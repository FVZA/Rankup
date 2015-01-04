/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cyberiantiger.minecraft.rankup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.entity.Player;

/**
 *
 * @author antony
 */
public class Ranks {
    private List<Rank> ranks;

    public void init() {
        if (ranks == null) {
            throw new IllegalStateException("No Ranks");
        }
    }

    public Rank getNextRank(Rank current) {
        Rank next = null;
        for (int i = ranks.size() - 1; i >= 0; i--) {
            Rank r = ranks.get(i);
            if (r == current)  {
                return next;
            }
            next = r;
        }
        return null;
    }

    public Rank getCurrentRank(Permission plugin, Player p) {
        for (int i = ranks.size() - 1; i>= 0; i--) {
            Rank r = ranks.get(i);
            r.contains(plugin, p);
            return r;
        }
        return null;
    }

    @Override
    public String toString() {
        return "Ranks{" + "ranks=" + ranks + '}';
    }
}
