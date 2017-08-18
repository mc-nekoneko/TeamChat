package com.rathserver.teamchat.configure;

import lombok.Getter;
import lombok.NonNull;
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
    private final String parent = "displaynames";
    private final Map<String, String> displayNames = new HashMap<>();
    private final List<String> exclusionTeams = new ArrayList<>();

    @Getter
    private boolean teamChat = false;

    public YamlConfig(@NonNull JavaPlugin plugin) {
        plugin.saveDefaultConfig();
        this.ymlFile = new File(plugin.getDataFolder(), "config.yml");
    }

    public void load() {
        displayNames.clear();
        try {
            load(ymlFile);

            teamChat = getBoolean("teamchat", true);

            List<String> exclusion = getStringList("exclusion");
            if (exclusion != null) {
                exclusionTeams.addAll(exclusion);
            }

            Set<String> teams = getConfigurationSection(parent).getKeys(false);
            teams.forEach(this::put);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            save(ymlFile);
            displayNames.forEach(this::save);
            set("exclusion", exclusionTeams);
            set("teamchat", teamChat);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Optional<String> getDisplayName(String team) {
        return Optional.ofNullable(displayNames.get(team));
    }

    public void setDisplayName(@NonNull String team, @NonNull String name) {
        if (team.isEmpty() || name.isEmpty()) {
            throw new IllegalArgumentException("team or name is empty");
        }

        displayNames.put(team, name);
    }

    public boolean isExclusionTeam(@NonNull String team) {
        return exclusionTeams.contains(team);
    }

    public void addExlusionTeam(@NonNull String name) {
        exclusionTeams.add(name);
    }

    private void put(String team) {
        String name = getString(parent + "." + team);
        if (name.isEmpty()) {
            return;
        }

        displayNames.put(team, name);
    }

    private void save(String team, String name) {
        set(parent + "." + team, name);
    }
}
