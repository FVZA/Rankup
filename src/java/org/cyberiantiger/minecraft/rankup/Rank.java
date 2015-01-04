package org.cyberiantiger.minecraft.rankup;

import java.util.List;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.entity.Player;

public class Rank {
    private final String name = null;
    private final double price = 0.0D;
    private final List<GroupAssignment> groups = null;

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public List<GroupAssignment> getGroups() {
        return groups;
    }

    public boolean contains(Permission perms, Player player) {
        if (groups != null && !groups.isEmpty()) {
            for (GroupAssignment group : groups) {
                if (!perms.playerInGroup(group.getWorld(), player.getName(), group.getGroup())) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "Rank{" + "name=" + name + ", price=" + price + ", groups=" + groups + '}';
    }
}
