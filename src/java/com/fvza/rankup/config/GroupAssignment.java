package com.fvza.rankup.config;

public class GroupAssignment {
    private final String group;
    private final String world;

    public GroupAssignment(String group, String world) {
        this.group = group;
        this.world = world;
    }

    public String getGroup() {
        return group;
    }

    public String getWorld() {
        return world;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 13 * hash + (this.group != null ? this.group.hashCode() : 0);
        hash = 13 * hash + (this.world != null ? this.world.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GroupAssignment other = (GroupAssignment) obj;
        if ((this.group == null) ? (other.group != null) : !this.group.equals(other.group)) {
            return false;
        }
        if ((this.world == null) ? (other.world != null) : !this.world.equals(other.world)) {
            return false;
        }
        return true;
    }
}
