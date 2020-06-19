package com.rathserver.teamchat.configure;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by Nekoneko on 2017/08/19.
 */
public class YamlConfig extends Utf8YamlConfiguration {

    private final File ymlFile;
    private final String displayNamesPrefix = "displaynames";
    private final String teamsColorsPrefix = "teamscolor";
    private final Map<String, String> displayNames = new HashMap<>();
    private final Map<String, ChatColor> teamsColor = new HashMap<>();
    private final List<String> exclusionTeams = new ArrayList<>();

    @Getter
    @Setter
    private boolean teamChat = false;

    public YamlConfig(@NonNull JavaPlugin plugin) {
        plugin.saveDefaultConfig();
        this.ymlFile = new File(plugin.getDataFolder(), "config.yml");
    }

    public void load() {
        this.displayNames.clear();
        try {
            this.load(this.ymlFile);

            this.teamChat = this.getBoolean("teamchat", true);

            List<String> exclusion = this.getStringList("exclusion");
            if (exclusion != null) {
                this.exclusionTeams.addAll(exclusion);
            }

            Set<String> teams = getConfigurationSection(this.displayNamesPrefix).getKeys(false);
            teams.forEach(this::put);

            Set<String> colors = getConfigurationSection(this.teamsColorsPrefix).getKeys(false);
            colors.forEach(this::putColor);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            this.displayNames.forEach((s, s2) -> this.save(this.displayNamesPrefix, s, s2));
            this.teamsColor.forEach((s, color) -> this.save(this.teamsColorsPrefix, s, color.name()));
            set("exclusion", this.exclusionTeams);
            set("teamchat", this.teamChat);
            save(this.ymlFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Optional<String> getDisplayName(String team) {
        return Optional.ofNullable(this.displayNames.get(team));
    }

    public void setDisplayName(@NonNull String team, @NonNull String name) {
        if (team.isEmpty() || name.isEmpty()) {
            throw new IllegalArgumentException("team or name is empty");
        }

        this.displayNames.put(team, name);
    }

    public ChatColor getTeamColor(String team) {
        if (!this.teamsColor.containsKey(team)) {
            return ChatColor.RESET;
        }
        return this.teamsColor.get(team);
    }

    public void setTeamColor(@NonNull String team, @NonNull ChatColor color) {
        if (team.isEmpty()) {
            throw new IllegalArgumentException("team or name is empty");
        }

        this.teamsColor.put(team, color);
    }

    public boolean isExclusionTeam(@NonNull String team) {
        return this.exclusionTeams.contains(team);
    }

    public void addExclusionTeam(@NonNull String name) {
        this.exclusionTeams.add(name);
    }

    private void put(String team) {
        String name = this.getString(this.displayNamesPrefix + "." + team);
        if (name.isEmpty()) {
            return;
        }

        this.displayNames.put(team, name);
    }

    private void putColor(String team) {
        String name = this.getString(this.teamsColorsPrefix + "." + team);
        if (name.isEmpty()) {
            return;
        }

        this.teamsColor.put(team, ChatColor.valueOf(name));
    }

    private void save(String parted, String team, String name) {
        this.set(parted + "." + team, name);
    }
}
