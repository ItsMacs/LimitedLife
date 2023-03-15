package eu.maxpi.fiverr.limitedlife.utils;

import eu.maxpi.fiverr.limitedlife.LimitedLife;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeManager {

    public static void manageTime(){
        if(!LimitedLife.isStarted) return;

        LimitedLife.timeRemaining.keySet().stream().filter(s -> Bukkit.getPlayer(s) != null).forEach(s -> {
            Player p = Bukkit.getPlayer(s);
            if(p == null) return;

            if(LimitedLife.timeRemaining.get(s) <= 0){
                LimitedLife.scoreboard.getTeams().forEach(t -> t.removeEntry(s));
                LimitedLife.timeRemaining.put(s, -1L);
                p.setGameMode(GameMode.SPECTATOR);

                for(ItemStack i : p.getInventory()){
                    if(i == null) continue;
                    if(i.getType() == Material.AIR) continue;
                    p.getWorld().dropItem(p.getLocation(), i);
                }
                p.getInventory().clear();

                p.sendTitle(PluginLoader.lang.get("ranout-title"), PluginLoader.lang.get("ranout-subtitle"), 10, 100, 10);
                p.playSound(p.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1, 1);

                Bukkit.getOnlinePlayers().forEach(p1 -> p1.sendMessage(PluginLoader.lang.get("player-eliminated").replace("%player%", p.getName())));
                return;
            }

            LimitedLife.timeRemaining.put(s, LimitedLife.timeRemaining.get(s) - 1);
            display(p);
        });

        LimitedLife.timeRemaining.values().removeIf(l -> l == -1L);
    }

    private static void display(Player p){
        LimitedLife.scoreboard.getTeams().forEach(t -> t.removeEntry(p.getName()));

        long timeRemaining = LimitedLife.timeRemaining.get(p.getName());
        if(timeRemaining > ((PluginLoader.startTime / 3) * 2)){ //Over 2/3 of the time - green
            LimitedLife.scoreboard.getTeam("green").addEntry(p.getName());
        }else if(timeRemaining > ((PluginLoader.startTime / 3))){ //Over 1/3 of the time - yellow
            LimitedLife.scoreboard.getTeam("yellow").addEntry(p.getName());
        }else{
            LimitedLife.scoreboard.getTeam("red").addEntry(p.getName());
        }

        TextComponent comp = new TextComponent();
        comp.setText(PluginLoader.lang.get("actionbar-msg").replace("%time%", new SimpleDateFormat("HH:mm:ss").format(new Date(LimitedLife.timeRemaining.get(p.getName()) * 1000L))));
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, comp);
    }

}