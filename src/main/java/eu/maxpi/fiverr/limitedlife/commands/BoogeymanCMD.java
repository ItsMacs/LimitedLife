package eu.maxpi.fiverr.limitedlife.commands;

import eu.maxpi.fiverr.limitedlife.utils.BoogeymanManager;
import eu.maxpi.fiverr.limitedlife.utils.PluginLoader;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BoogeymanCMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission("limitedlife.admin")){
            sender.sendMessage(PluginLoader.lang.get("no-permission"));
            return true;
        }

        if(args.length == 0){
            sender.sendMessage(PluginLoader.lang.get("boogeyman-usage"));
            return true;
        }

        switch (args[0].toLowerCase()){
            case "cure" -> {
                if(args.length != 2){
                    sender.sendMessage(PluginLoader.lang.get("boogeyman-cure-usage"));
                    return true;
                }

                BoogeymanManager.boogeyMen.remove(args[1]);
                sender.sendMessage(PluginLoader.lang.get("boogeyman-cured"));
            }

            case "choose" -> {
                BoogeymanManager.choose();
                sender.sendMessage(PluginLoader.lang.get("boogeyman-chosen"));
            }
        }
        return true;
    }

}
