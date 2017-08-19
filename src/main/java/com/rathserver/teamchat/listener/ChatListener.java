package com.rathserver.teamchat.listener;

import com.rathserver.teamchat.TeamChatPlugin;
import com.rathserver.teamchat.util.StringUtil;
import lombok.RequiredArgsConstructor;
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
        event.setFormat(StringUtil.coloring("%s&7:&r %s"));
        if (plugin.getYamlConfig().isTeamChat()) {
            teamChat(event);
        }
    }

    private void teamChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Team team = getTeam(player);
        if (team == null) {
            return;
        }
        if (plugin.getYamlConfig().isExclusionTeam(team.getName())) {
            return;
        }

        event.setFormat(format(team));
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
        return board != null ? board.getEntryTeam(player.getName()) : null;
    }

    private String format(Team team) {
        String displayName = plugin.getYamlConfig().getDisplayName(team.getName()).orElse(team.getDisplayName());
        return StringUtil.coloring("&7[&r" + displayName + "&r&7]&r %s&7:&r %s");
    }
}
