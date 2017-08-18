package com.rathserver.teamchat.listener;

import com.rathserver.teamchat.TeamChatPlugin;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Set;

/**
 * Created by Nekoneko on 2017/08/19.
 */
@RequiredArgsConstructor
public final class ChatListener implements Listener {

    private final TeamChatPlugin plugin;

    @EventHandler(ignoreCancelled = true)
    public void asyncPlayerChat(AsyncPlayerChatEvent event) {
        if (!plugin.getYamlConfig().isTeamChat()) {
            return;
        }

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
        recipients.stream().filter(o -> hasEntryAndNotOp(team, o)).forEach(recipients::remove);
    }

    private boolean hasEntryAndNotOp(Team team, Player player) {
        return team.hasEntry(player.getName()) && !player.isOp();
    }

    private Team getTeam(Player player) {
        Scoreboard board = player.getScoreboard();
        return board != null ? board.getEntryTeam(player.getName()) : null;
    }

    private String format(Team team) {
        String displayName = plugin.getYamlConfig().getDisplayName(team.getName()).orElse(team.getDisplayName());
        return colored("&7[&r" + displayName + "&r&7]&r %s&7:&r %s");
    }

    private String colored(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }
}
