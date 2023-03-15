package eu.maxpi.fiverr.limitedlife.utils;

import eu.maxpi.fiverr.limitedlife.LimitedLife;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;

public class PluginLoader {

    public static HashMap<String, String> lang = new HashMap<>();

    public static long deathBuff;
    public static long killBuff;
    public static boolean includePlayers;
    public static long startTime;

    public static void load(){
        LimitedLife.getInstance().saveResource("config.yml", false);

        YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(LimitedLife.getInstance().getDataFolder() + "/config.yml"));
        config.getConfigurationSection("lang").getKeys(false).forEach(s -> lang.put(s, ColorTranslator.translate(config.getString("lang." + s))));

        deathBuff = config.getLong("death-penalty");
        killBuff = config.getLong("kill-buff");
        includePlayers = config.getBoolean("include-players");
        startTime = config.getLong("start-time");
    }

}
