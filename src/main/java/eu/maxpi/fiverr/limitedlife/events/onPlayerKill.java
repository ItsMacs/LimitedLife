package eu.maxpi.fiverr.limitedlife.events;

import eu.maxpi.fiverr.limitedlife.LimitedLife;
import eu.maxpi.fiverr.limitedlife.utils.BoogeymanManager;
import eu.maxpi.fiverr.limitedlife.utils.PluginLoader;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.List;

public class onPlayerKill implements Listener {

    public static List<String> awaitingAnnounce = new ArrayList<>();

    @EventHandler
    public void playerKill(EntityDamageByEntityEvent event){
        if(!LimitedLife.isStarted) return;
        if(!(event.getEntity() instanceof Player p)) return;

        if(p.getHealth() - event.getFinalDamage() > 0) return;

        awaitingAnnounce.add(p.getName());

        if(!(event.getDamager() instanceof Player) && !(event.getDamager() instanceof Projectile)) return;
        if(event.getDamager() instanceof Projectile && !(((Projectile)event.getDamager()).getShooter() instanceof Player)) return;

        Player killer = event.getDamager() instanceof Player ? (Player)event.getDamager() : (Player)((Projectile)event.getDamager()).getShooter();
        if(killer == null) return;

        boolean boogey = BoogeymanManager.boogeyMen.contains(killer.getName());
        if(boogey) {
            p.setMetadata("boogeykilled", new FixedMetadataValue(LimitedLife.getInstance(), true));
            BoogeymanManager.boogeyMen.remove(killer.getName());
        }

        LimitedLife.timeRemaining.put(killer.getName(), LimitedLife.timeRemaining.get(killer.getName()) + (PluginLoader.killBuff * (boogey ? 2 : 1)));
        killer.sendTitle(PluginLoader.lang.get(boogey ? "killer-boogey" : "killer-title"), PluginLoader.lang.get("killer-subtitle"), 20, 160, 20);
    }

    @EventHandler
    public void playerKill(EntityDamageEvent event){
        if(!LimitedLife.isStarted) return;
        if(event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) return; //Managed above

        if(!(event.getEntity() instanceof Player p)) return;

        if(p.getHealth() - event.getFinalDamage() > 0) return;

        awaitingAnnounce.add(p.getName());
    }

    @EventHandler
    public void playerRefresh(PlayerRespawnEvent event){
        if(!LimitedLife.timeRemaining.containsKey(event.getPlayer().getName())){
            event.getPlayer().setGameMode(GameMode.SPECTATOR);
            return;
        }

        if(!awaitingAnnounce.contains(event.getPlayer().getName())) return;

        boolean boogey = event.getPlayer().hasMetadata("boogeykilled");
        event.getPlayer().removeMetadata("boogeykilled", LimitedLife.getInstance());

        LimitedLife.timeRemaining.put(event.getPlayer().getName(), LimitedLife.timeRemaining.get(event.getPlayer().getName()) - (PluginLoader.deathBuff * (boogey ? 2 : 1)));
        event.getPlayer().sendTitle(PluginLoader.lang.get(boogey ? "killed-boogey-title" : "killed-title"), PluginLoader.lang.get(boogey ? "killed-boogey-subtitle" : "killed-subtitle"), 20, 160, 20);

        awaitingAnnounce.remove(event.getPlayer().getName());
    }

}
