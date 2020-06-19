package com.rathserver.teamchat.listener;

import com.rathserver.teamchat.TeamChatPlugin;
import com.rathserver.teamchat.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Nekoneko on 2017/08/19.
 */
@RequiredArgsConstructor
public final class ChatListener implements Listener {

    private final TeamChatPlugin plugin;

    @EventHandler(ignoreCancelled = true)
    public void asyncPlayerChat(AsyncPlayerChatEvent event) {
        if (this.plugin.getYamlConfig().isTeamChat()) {
            this.teamChat(event);
        } else {
            Player player = event.getPlayer();
            Team team = player.getScoreboard().getEntryTeam(player.getName());
            ChatColor color = team != null ? team.getColor() : ChatColor.RESET;
            event.setFormat(StringUtil.coloring(color + "%s&7:&r %s"));
        }
    }

    private void teamChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Team team = this.getTeam(player);
        if (team == null) {
            return;
        }
        if (this.plugin.getYamlConfig().isExclusionTeam(team.getName())) {
            return;
        }

        event.setFormat(this.format(team));
        Set<Player> recipients = event.getRecipients();
        recipients.removeAll(exclusion(player, team, recipients));
    }

    private Collection<Player> exclusion(Player sender, Team senderTeam, Set<Player> recipients) {
        return recipients.stream().filter(player -> !player.equals(sender)).filter(player -> notEntryAndNotHavePermission(player, senderTeam)).collect(Collectors.toSet());
    }

    private boolean notEntryAndNotHavePermission(Player player, Team team) {
        return !team.hasEntry(player.getName()) && !player.hasPermission("teamchat.bypass");
    }

    private Team getTeam(Player player) {
        Scoreboard board = player.getScoreboard();
        return board.getEntryTeam(player.getName());
    }

    private String format(Team team) {
        String displayName = team.getDisplayName();
        ChatColor color = team.getColor();
        return StringUtil.coloring("&7[&r" + displayName + "&r&7]&r " + color + "%s&7:&r %s");
    }
}
