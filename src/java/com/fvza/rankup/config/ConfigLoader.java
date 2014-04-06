package com.fvza.rankup.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigLoader {

    public static List<Rank> loadRanks(FileConfiguration config) {
        List<Rank> ranks = new ArrayList<Rank>();
        ConfigurationSection ranksSection = config.getConfigurationSection("ranks");
        if (ranksSection != null) {
            for (String key : ranksSection.getKeys(false)) {
                ConfigurationSection rankSection = ranksSection.getConfigurationSection(key);
                double price = rankSection.getDouble("price", 0.0D);
                List<GroupAssignment> groups = new ArrayList<GroupAssignment>();
                for (String s : rankSection.getStringList("groups")) {
                    String group;
                    String world;
                    int i = s.indexOf('@');
                    if (i != -1) {
                        group = s.substring(0, i);
                        world = s.substring(i+1);
                    } else {
                        group = s;
                        world = null;
                    }
                    groups.add(new GroupAssignment(group, world));
                }
                ranks.add(new Rank(key, price, groups));
            }
        }
        return ranks;
    }

    public static Map<String, String> loadTranslations(FileConfiguration config) {
        Map<String, String> translations = new HashMap<String, String>();
        ConfigurationSection languages = config.getConfigurationSection("language");
        for (String s : languages.getKeys(true)) {
            if (languages.isString(s)) {
                String format = languages.getString(s);
                format = format.replace('&', '\u00a7');
                translations.put(s, format);
            }
        }
        return translations;
    }
    
}
