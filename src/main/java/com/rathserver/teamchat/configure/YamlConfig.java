package com.rathserver.teamchat.configure;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nekoneko on 2017/08/19.
 */
public class YamlConfig extends Utf8YamlConfiguration {

    private final File ymlFile;
    private final List<String> exclusionTeams = new ArrayList<>();

    @Getter
    @Setter
    private boolean teamChat = false;

    public YamlConfig(@NonNull JavaPlugin plugin) {
        plugin.saveDefaultConfig();
        this.ymlFile = new File(plugin.getDataFolder(), "config.yml");
    }

    public void load() {
        try {
            this.load(this.ymlFile);

            this.teamChat = this.getBoolean("teamchat", true);

            List<String> exclusion = this.getStringList("exclusion");
            this.exclusionTeams.addAll(exclusion);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            this.set("exclusion", this.exclusionTeams);
            this.set("teamchat", this.teamChat);
            this.save(this.ymlFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isExclusionTeam(@NonNull String team) {
        return this.exclusionTeams.contains(team);
    }

    public void addExclusionTeam(@NonNull String name) {
        this.exclusionTeams.add(name);
    }
}
