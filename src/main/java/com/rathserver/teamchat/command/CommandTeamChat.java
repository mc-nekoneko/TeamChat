package com.rathserver.teamchat.command;

import com.rathserver.teamchat.TeamChatPlugin;
import com.rathserver.teamchat.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Nekoneko on 2017/08/19.
 */
@RequiredArgsConstructor
public class CommandTeamChat implements TabExecutor {

    private final TeamChatPlugin plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!sender.hasPermission("teamchat.command")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission.");
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(ChatColor.YELLOW + cmd.getUsage());
            return true;
        }
        if (args[0].equalsIgnoreCase("displayname")) {
            return this.displayName(sender, args);
        }
        if (args[0].equalsIgnoreCase("exclusion")) {
            return this.exclusion(sender, args);
        }
        if (args[0].equalsIgnoreCase("color")) {
            return this.color(sender, args);
        }

        boolean teamChat = bool(args[0]);
        this.plugin.getYamlConfig().setTeamChat(teamChat);
        this.plugin.getYamlConfig().save();
        sender.sendMessage(ChatColor.GREEN + (teamChat ? "Team chat has been activated" : "Team chat has been deactivated"));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String s, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("on", "off", "displayname", "exclusion", "color");
        }
        if (args.length == 2) {
            Scoreboard board = this.plugin.getServer().getScoreboardManager().getMainScoreboard();
            return board.getTeams().parallelStream().map(Team::getName).collect(Collectors.toList());
        }
        return null;
    }

    private boolean displayName(CommandSender sender, String[] args) {
        if (args.length <= 1) {
            sender.sendMessage(ChatColor.YELLOW + "/teamchat displayname <team> <display_name>");
            return true;
        }

        String team = args[1];
        if (team.length() > 16) {
            sender.sendMessage(ChatColor.RED + String.format("The name '%s' is too long for a team, it can be at most 16 characters long", team));
            return true;
        }

        String displayName = StringUtil.join(" ", 2, args);
        this.plugin.getYamlConfig().setDisplayName(team, displayName);
        this.plugin.getYamlConfig().save();
        sender.sendMessage(ChatColor.GREEN + String.format("The display name of '%s' team has been changed to '%s'", team, displayName));
        return true;
    }

    private boolean exclusion(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ChatColor.YELLOW + "/teamchat exclusion <team>");
            return true;
        }

        String team = args[1];
        if (team.length() > 16) {
            sender.sendMessage(ChatColor.RED + String.format("The name '%s' is too long for a team, it can be at most 16 characters long", team));
            return true;
        }

        this.plugin.getYamlConfig().addExclusionTeam(team);
        this.plugin.getYamlConfig().save();
        sender.sendMessage(ChatColor.GREEN + String.format("'%s' team has been excluded from team chat", team));
        return true;
    }

    private boolean color(CommandSender sender, String[] args) {
        if (args.length <= 1) {
            sender.sendMessage(ChatColor.YELLOW + "/teamchat color <team> <color>");
            return true;
        }

        String team = args[1];
        if (team.length() > 16) {
            sender.sendMessage(ChatColor.RED + String.format("The name '%s' is too long for a team, it can be at most 16 characters long", team));
            return true;
        }

        ChatColor color;
        try {
            color = ChatColor.valueOf(args[2]);
        } catch (IllegalArgumentException ex) {
            sender.sendMessage(ChatColor.RED + ex.getLocalizedMessage());
            return true;
        }

        this.plugin.getYamlConfig().setTeamColor(team, color);
        this.plugin.getYamlConfig().save();
        sender.sendMessage(ChatColor.GREEN + "");
        return true;
    }

    private boolean bool(String text) {
        switch (text) {
            case "true":
            case "on":
            case "y":
                return true;
        }

        return false;
    }
}
