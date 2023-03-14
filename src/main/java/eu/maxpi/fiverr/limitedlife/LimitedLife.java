package eu.maxpi.fiverr.limitedlife;

import eu.maxpi.fiverr.limitedlife.utils.PluginLoader;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class LimitedLife extends JavaPlugin {

    private static LimitedLife instance = null;
    public static LimitedLife getInstance() { return LimitedLife.instance; }
    private static void setInstance(LimitedLife in) { LimitedLife.instance = in; }

    @Override
    public void onEnable() {
        setInstance(this);

        PluginLoader.load();

        loadTasks();
        loadCommands();
        loadEvents();

        Bukkit.getLogger().info("LimitedLife by fiverr.com/macslolz was enabled successfully!");
    }

    private void loadTasks(){

    }

    private void loadCommands(){

    }

    private void loadEvents(){

    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("LimitedLife by fiverr.com/macslolz was disabled successfully!");
    }
}
