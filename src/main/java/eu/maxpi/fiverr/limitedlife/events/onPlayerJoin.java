package eu.maxpi.fiverr.limitedlife.events;

import eu.maxpi.fiverr.limitedlife.LimitedLife;
import eu.maxpi.fiverr.limitedlife.utils.PluginLoader;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class onPlayerJoin implements Listener {

    @EventHandler
    public void playerJoin(PlayerJoinEvent event){
        if(LimitedLife.scoreboard != null) event.getPlayer().setScoreboard(LimitedLife.scoreboard);

        if(!PluginLoader.includePlayers && LimitedLife.isStarted) {
            event.getPlayer().setGameMode(GameMode.SPECTATOR);
            return;
        }

        if(LimitedLife.timeRemaining.containsKey(event.getPlayer().getName())) return;

        LimitedLife.timeRemaining.put(event.getPlayer().getName(), PluginLoader.startTime);
    }

}
