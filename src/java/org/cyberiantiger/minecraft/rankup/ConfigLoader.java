package org.cyberiantiger.minecraft.rankup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.CustomClassLoaderConstructor;
import org.yaml.snakeyaml.introspector.BeanAccess;

public class ConfigLoader {
    private static final String RANKS_CONFIG = "config.yml";
    private static final String LANGUAGE_CONFIG = "language.yml";

    private static File saveDefaultConfig(Main plugin, String config) throws IOException {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }
        File result = new File(plugin.getDataFolder(), config);
        if (!result.exists()) {
            Files.copy(ConfigLoader.class.getClassLoader().getResourceAsStream(config), result.toPath());
        }
        return result;
    }

    private static Map<String, String> flatten(Map<String, String> out, Map<? extends Object, ? extends Object> in, String prefix) {
        for (Map.Entry<? extends Object, ? extends Object> e : in.entrySet()) {
            if (e.getValue() instanceof Map) {
                flatten(out, (Map<? extends Object, ? extends Object>) e.getValue(), prefix + e.getKey() + ".");
            } else {
                out.put(prefix + e.getKey(), String.valueOf(e.getValue()).replace('&', '\u00a7'));
            }
        }
        return out;
    }

    public static Ranks loadRanks(Main plugin) throws FileNotFoundException, IOException {
        File file = saveDefaultConfig(plugin, RANKS_CONFIG);
        Yaml configLoader = new Yaml(new CustomClassLoaderConstructor(Ranks.class, ConfigLoader.class.getClassLoader()));
        configLoader.setBeanAccess(BeanAccess.FIELD);
        return configLoader.loadAs(new FileReader(file), Ranks.class);
    }

    public static Map<String, String> loadTranslations(Main plugin) throws IOException {
        File saveFile = saveDefaultConfig(plugin, LANGUAGE_CONFIG);
        Yaml configLoader = new Yaml(new CustomClassLoaderConstructor(ConfigLoader.class.getClassLoader()));
        return flatten(new HashMap<String,String>(), (Map<? extends Object,? extends Object>)configLoader.load(new FileReader(saveFile)), "");
    }
}