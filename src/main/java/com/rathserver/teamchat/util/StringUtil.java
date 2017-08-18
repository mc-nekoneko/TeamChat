package com.rathserver.teamchat.util;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

/**
 * Created by Nekoneko on 2017/08/19.
 */
@UtilityClass
public class StringUtil {

    public String join(String separator, int args, String... parts) {
        StringBuilder builder = new StringBuilder(parts.length * 16);
        for (int i = args; i < parts.length; i++) {
            if (i != args) {
                builder.append(separator);
            }
            builder.append(parts[i]);
        }
        return builder.toString();
    }

    public String coloring(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }
}
