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

    /**
     * Executes enum valueOf, if that throws an error, a default is returned.
     *
     * @param name The name to be used.
     * @param def  The default value.
     * @param <T>  The enum type.
     * @return Parsed enum or default.
     */
    @SuppressWarnings("unchecked")
    public static <T extends Enum<T>> T valueOfOr(final String name, T def) {
        assert def != null : "Default value cannot be null.";

        return valueOfOr((Class<T>) def.getClass(), name, def);
    }

    /**
     * Executes enum valueOf, if that throws an error, a default is returned.
     *
     * @param clazz The enum class
     * @param name  The name to be used.
     * @param def   The default value.
     * @param <T>   The enum type.
     * @return Parsed enum or default.
     */
    public static <T extends Enum<T>> T valueOfOr(final Class<T> clazz, final String name, T def) {
        try {
            return Enum.valueOf(clazz, name);
        } catch (IllegalArgumentException e) {
            return def;
        }
    }
}
