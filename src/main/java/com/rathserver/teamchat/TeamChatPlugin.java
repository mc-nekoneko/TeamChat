package com.rathserver.teamchat;

import com.rathserver.teamchat.command.CommandTeamChat;
import com.rathserver.teamchat.configure.YamlConfig;
import com.rathserver.teamchat.listener.ChatListener;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class TeamChatPlugin extends JavaPlugin {

    @Getter
    private YamlConfig yamlConfig;

    @Override
    public void onEnable() {
        super.onEnable();
        yamlConfig = new YamlConfig(this);
        yamlConfig.load();
        getCommand("teamchat").setExecutor(new CommandTeamChat(this));
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        yamlConfig.save();
    }
}
