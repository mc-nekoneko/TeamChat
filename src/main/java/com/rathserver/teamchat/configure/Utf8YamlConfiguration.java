package com.rathserver.teamchat.configure;

import com.google.common.io.Files;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Utf8YamlConfiguration extends YamlConfiguration {

    @Override
    public void load(File file) throws IOException, InvalidConfigurationException {
        super.load(file);
    }

    @Override
    public void load(Reader reader) throws IOException, InvalidConfigurationException {
        Validate.notNull(reader, "Stream cannot be null");

        StringBuilder builder = new StringBuilder();
        try (BufferedReader input = new BufferedReader(reader)) {
            String line;

            while ((line = input.readLine()) != null) {
                builder.append(line);
                builder.append('\n');
            }
        }

        this.loadFromString(builder.toString());
    }

    @Override
    public void save(File file) throws IOException {
        Validate.notNull(file, "File cannot be null");

        Files.createParentDirs(file);
        String data = this.saveToString();
        FileOutputStream stream = new FileOutputStream(file);
        try (OutputStreamWriter writer = new OutputStreamWriter(stream, StandardCharsets.UTF_8)) {
            writer.write(data);
        }
    }
}
