package eu.maxpi.fiverr.limitedlife.utils;

import eu.maxpi.fiverr.limitedlife.LimitedLife;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class BoogeymanManager {

    public static int countdown = 9999; //in case manage starts before init, so that it cannot choose a boogeyman at the start, that'd be pretty bad

    //K: Player name
    //V: has killed anyone as boogeyman
    public static List<String> boogeyMen = new ArrayList<>();
    public static Random random = new Random();

    public static void init(){
        countdown = 2700 + random.nextInt(900); //Random amount between 45 minutes and 60 minutes
        boogeyMen.clear();
    }

    public static void manage(){
        countdown--;

        if(boogeyMen.size() == 0){
            switch (countdown){
                case 600,300,60 -> {
                    Bukkit.getOnlinePlayers().forEach(p -> {
                        p.sendMessage(PluginLoader.lang.get("boogeyman-taunt-timer").replace("%time%", String.valueOf(countdown / 60)));
                        p.playSound(p.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1, 1);
                    });
                }

                case 35 -> {//boogeyman is about to be chosen (chat)
                    Bukkit.getOnlinePlayers().forEach(p -> {
                        p.sendMessage(PluginLoader.lang.get("boogeyman-last-minute-timer"));
                        p.playSound(p.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1, 1);
                    });
                }

                case 20 -> { //boogeyman is about to be chosen (title)
                    Bukkit.getOnlinePlayers().forEach(p -> {
                        p.sendTitle(PluginLoader.lang.get("boogeyman-last-minute-timer"), PluginLoader.lang.get("boogeyman-taunt-timer-subtitle"), 15, 60, 15);
                        p.playSound(p.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1, 1);
                    });
                }

                case 13 -> { //3
                    Bukkit.getOnlinePlayers().forEach(p -> {
                        p.sendTitle(PluginLoader.lang.get("boogeyman-taunt-3"), "", 15, 20, 15);
                        p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                    });
                }

                case 10 -> { //2
                    Bukkit.getOnlinePlayers().forEach(p -> {
                        p.sendTitle(PluginLoader.lang.get("boogeyman-taunt-2"), "", 15, 20, 15);
                        p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                    });
                }

                case 7 -> { //1
                    Bukkit.getOnlinePlayers().forEach(p -> {
                        p.sendTitle(PluginLoader.lang.get("boogeyman-taunt-1"), "", 15, 20, 15);
                        p.playSound(p.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                    });
                }

                case 5 -> { //you are
                    Bukkit.getOnlinePlayers().forEach(p -> {
                        p.sendTitle(PluginLoader.lang.get("boogeyman-taunt-youare"), "", 15, 20, 15);
                        p.playSound(p.getLocation(), Sound.BLOCK_BEACON_AMBIENT, 1, 1);
                    });
                }

                case 0 -> { //Boogeyman choose
                    choose();
                }
            }
            return;
        }

        if(countdown > 0) return;

        boogeyMen.forEach(s -> {
            long d = LimitedLife.timeRemaining.get(s) / (PluginLoader.startTime / 3);
            LimitedLife.timeRemaining.put(s, d * (PluginLoader.startTime / 3)); //Snap to the nearest third

            Player p = Bukkit.getPlayer(s);
            if(p == null) return;

            p.sendMessage(PluginLoader.lang.get("boogeyman-lost"));
            p.sendTitle(PluginLoader.lang.get("boogeyman-lost-title"), PluginLoader.lang.get("boogeyman-lost-subtitle"), 0, 80, 0);
            p.playSound(p.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1, 1);
        });
        init();
    }

    public static void choose(){
        for(int i = 0; i < random.nextInt(3) + 1; i++){
            List<? extends Player> l = Bukkit.getOnlinePlayers().stream().filter(p1 -> !boogeyMen.contains(p1.getName()) && LimitedLife.timeRemaining.containsKey(p1.getName())).toList();
            if(l.size() == 0) break;

            Player p = l.get(random.nextInt(l.size()));
            if(p == null) break;

            boogeyMen.add(p.getName());
            p.sendTitle(PluginLoader.lang.get("boogeyman"), "", 0, 80, 0);
            p.sendMessage(PluginLoader.lang.get("boogeyman-tutorial"));
            p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_AMBIENT, 1, 1);
        }

        Bukkit.getOnlinePlayers().stream().filter(p -> !boogeyMen.contains(p.getName())).forEach(p -> {
            p.sendTitle(PluginLoader.lang.get("not-boogeyman"), "", 0, 80, 0);
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
        });
        countdown = 1800;
    }

}
