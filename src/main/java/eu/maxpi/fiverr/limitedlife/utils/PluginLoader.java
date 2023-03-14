package eu.maxpi.fiverr.limitedlife.utils;

import eu.maxpi.fiverr.limitedlife.LimitedLife;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;

public class PluginLoader {

    public static HashMap<String, String> lang = new HashMap<>();

    public static void load(){
        LimitedLife.getInstance().saveResource("config.yml", false);

        YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(LimitedLife.getInstance().getDataFolder() + "/config.yml"));
        config.getConfigurationSection("lang").getKeys(false).forEach(s -> lang.put(s, ColorTranslator.translate(config.getString("lang." + s))));


    }

}
