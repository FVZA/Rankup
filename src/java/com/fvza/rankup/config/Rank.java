package com.fvza.rankup.config;

import java.util.List;

public class Rank {
    private final String name;
    private final double price;
    private final List<GroupAssignment> groups;
    
    public Rank(String name, double price, List<GroupAssignment> groups) {
        this.name = name;
        this.price = price;
        this.groups = groups;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public List<GroupAssignment> getGroups() {
        return groups;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 67 * hash + (int) (Double.doubleToLongBits(this.price) ^ (Double.doubleToLongBits(this.price) >>> 32));
        hash = 67 * hash + (this.groups != null ? this.groups.hashCode() : 0);
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
        final Rank other = (Rank) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if (Double.doubleToLongBits(this.price) != Double.doubleToLongBits(other.price)) {
            return false;
        }
        if (this.groups != other.groups && (this.groups == null || !this.groups.equals(other.groups))) {
            return false;
        }
        return true;
    }
}
