package com.rathserver.teamchat;

import com.rathserver.teamchat.command.CommandGlobalChat;
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
        this.yamlConfig = new YamlConfig(this);
        this.yamlConfig.load();
        this.getCommand("globalchat").setExecutor(new CommandGlobalChat());
        this.getCommand("teamchat").setExecutor(new CommandTeamChat(this));
        this.getServer().getPluginManager().registerEvents(new ChatListener(this), this);
    }
}
