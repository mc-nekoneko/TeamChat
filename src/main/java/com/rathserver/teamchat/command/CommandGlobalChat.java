package com.rathserver.teamchat.command;

import com.rathserver.teamchat.util.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Nekoneko on 2017/08/19.
 */
public class CommandGlobalChat implements CommandExecutor {

    private static final String TEMPLATE = StringUtil.coloring("%s&7:&r %s");

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "can be executed only from player");
            return true;
        }
        if (!sender.hasPermission("teamchat.globalchat")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission.");
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(ChatColor.YELLOW + cmd.getUsage());
            return true;
        }

        Bukkit.broadcastMessage(format(sender.getName(), StringUtil.join(" ", 0, args)));
        return true;
    }

    private String format(String name, String message) {
        return String.format(TEMPLATE, name, message);
    }
}
