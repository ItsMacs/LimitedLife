package eu.maxpi.fiverr.limitedlife.commands;

import eu.maxpi.fiverr.limitedlife.LimitedLife;
import eu.maxpi.fiverr.limitedlife.utils.BoogeymanManager;
import eu.maxpi.fiverr.limitedlife.utils.PluginLoader;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.RenderType;

public class TimerCMD implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission("limitedlife.admin")){
            sender.sendMessage(PluginLoader.lang.get("no-permission"));
            return true;
        }

        if(args.length == 0){
            sender.sendMessage(PluginLoader.lang.get("timer-usage"));
            return true;
        }

        switch (args[0].toLowerCase()){
            case "start" -> {
                if(LimitedLife.isStarted){
                    sender.sendMessage(PluginLoader.lang.get("already-started"));
                    return true;
                }

                LimitedLife.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

                LimitedLife.scoreboard.registerNewTeam("green").setColor(ChatColor.GREEN);
                LimitedLife.scoreboard.registerNewTeam("yellow").setColor(ChatColor.YELLOW);
                LimitedLife.scoreboard.registerNewTeam("red").setColor(ChatColor.RED);

                Bukkit.getOnlinePlayers().forEach(p -> {
                    p.setScoreboard(LimitedLife.scoreboard);

                    p.getInventory().clear();

                    p.teleport(p.getWorld().getSpawnLocation());

                    p.setGameMode(GameMode.SURVIVAL);

                    LimitedLife.timeRemaining.put(p.getName(), PluginLoader.startTime);
                    p.sendMessage(PluginLoader.lang.get("timer-started"));
                    p.sendTitle(PluginLoader.lang.get("timer-started-title"), PluginLoader.lang.get("timer-started-subtitle"), 15, 80, 15);

                    p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
                });

                BoogeymanManager.init();
                LimitedLife.isStarted = true;
            }

            case "pause" -> {
                Bukkit.getOnlinePlayers().forEach(p -> {
                    p.sendMessage(PluginLoader.lang.get("timer-stopped"));
                    p.sendTitle(PluginLoader.lang.get("timer-stopped-title"), PluginLoader.lang.get("timer-stopped-subtitle"), 15, 80, 15);

                    p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
                });

                LimitedLife.isStarted = false;
            }

            case "stop" -> {
                Bukkit.getOnlinePlayers().forEach(p -> {
                    p.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
                    LimitedLife.scoreboard = null;

                    p.setGameMode(GameMode.SURVIVAL);

                    p.sendMessage(PluginLoader.lang.get("timer-stopped"));
                    p.sendTitle(PluginLoader.lang.get("timer-stopped-title"), PluginLoader.lang.get("timer-stopped-subtitle"), 15, 80, 15);

                    p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
                });

                LimitedLife.timeRemaining.clear();
                LimitedLife.isStarted = false;
            }

            case "resume" -> {
                Bukkit.getOnlinePlayers().forEach(p -> {
                    p.sendMessage(PluginLoader.lang.get("timer-started"));
                    p.sendTitle(PluginLoader.lang.get("timer-started-title"), PluginLoader.lang.get("timer-started-subtitle"), 15, 80, 15);

                    p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
                });

                LimitedLife.isStarted = true;
            }

            case "restart" -> {
                Bukkit.getOnlinePlayers().forEach(p -> {
                    p.setScoreboard(LimitedLife.scoreboard);

                    p.getInventory().clear();

                    p.teleport(p.getWorld().getSpawnLocation());

                    p.setGameMode(GameMode.SURVIVAL);
                    LimitedLife.timeRemaining.put(p.getName(), PluginLoader.startTime);
                    p.sendMessage(PluginLoader.lang.get("timer-restarted"));
                    p.sendTitle(PluginLoader.lang.get("timer-restarted-title"), PluginLoader.lang.get("timer-restarted-subtitle"), 15, 80, 15);

                    p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);
                });

                BoogeymanManager.init();
                LimitedLife.isStarted = true;
            }
        }
        return true;
    }
}
