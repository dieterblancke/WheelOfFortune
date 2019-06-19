package com.dbsoftwares.dangerwheel.utils;

import com.dbsoftwares.configuration.api.IConfiguration;
import com.dbsoftwares.dangerwheel.DangerWheel;
import net.md_5.bungee.api.ChatColor;

public class Utils {

    private Utils() {
    }

    public static String c(final String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String getMessage(final String path) {
        final IConfiguration config = DangerWheel.getInstance().getConfiguration();

        return Utils.c(getPrefix() + config.getString("messages." + path));
    }

    public static String prefixedMessage(final String message) {
        return Utils.c(getPrefix() + message);
    }

    public static String getPrefix() {
        return c(DangerWheel.getInstance().getConfiguration().getString("messages.prefix"));
    }
}
