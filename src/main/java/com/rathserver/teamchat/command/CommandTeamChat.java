package com.rathserver.teamchat.command;

import com.rathserver.teamchat.TeamChatPlugin;
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
        if (args[0].equalsIgnoreCase("exclusion")) {
            return this.exclusion(sender, args);
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
            return Arrays.asList("on", "off", "exclusion");
        }
        if (args.length == 2) {
            Scoreboard board = this.plugin.getServer().getScoreboardManager().getMainScoreboard();
            return board.getTeams().parallelStream().map(Team::getName).collect(Collectors.toList());
        }
        return null;
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
