package eu.maxpi.fiverr.limitedlife;

import eu.maxpi.fiverr.limitedlife.commands.BoogeymanCMD;
import eu.maxpi.fiverr.limitedlife.commands.TimerCMD;
import eu.maxpi.fiverr.limitedlife.events.onPlayerJoin;
import eu.maxpi.fiverr.limitedlife.events.onPlayerKill;
import eu.maxpi.fiverr.limitedlife.utils.BoogeymanManager;
import eu.maxpi.fiverr.limitedlife.utils.PluginLoader;
import eu.maxpi.fiverr.limitedlife.utils.TimeManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public final class LimitedLife extends JavaPlugin {

    private static LimitedLife instance = null;
    public static LimitedLife getInstance() { return LimitedLife.instance; }
    private static void setInstance(LimitedLife in) { LimitedLife.instance = in; }

    public static Scoreboard scoreboard;

    public static HashMap<String, Long> timeRemaining = new HashMap<>();
    public static boolean isStarted = false;

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
        new BukkitRunnable() {
            @Override
            public void run() {
                if(!isStarted) return;

                TimeManager.manageTime();
                BoogeymanManager.manage();

                Bukkit.getOnlinePlayers().forEach(p -> {
                    Arrays.stream(p.getInventory().getArmorContents()).filter(i -> i != null && i.getType().name().contains("HELMET")).forEach(i -> i.setType(Material.AIR));
                });
            }
        }.runTaskTimer(this, 0L, 20L);
    }

    private void loadCommands(){
        Objects.requireNonNull(getCommand("timer")).setExecutor(new TimerCMD());
        Objects.requireNonNull(getCommand("boogeyman")).setExecutor(new BoogeymanCMD());
    }

    private void loadEvents(){
        Bukkit.getPluginManager().registerEvents(new onPlayerJoin(), this);
        Bukkit.getPluginManager().registerEvents(new onPlayerKill(), this);
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("LimitedLife by fiverr.com/macslolz was disabled successfully!");
    }
}
