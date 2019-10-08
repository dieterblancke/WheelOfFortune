package com.dbsoftwares.wheeloffortune.utils;

import com.dbsoftwares.configuration.api.IConfiguration;
import com.dbsoftwares.wheeloffortune.WheelOfFortune;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

public class Utils {

    public static final String OPTED_IN_KEY = "DW_OPTED_IN";

    private Utils() {
    }

    public static void setMetaData(final Player player, final String key, final Object value) {
        // Removing first to be sure
        player.removeMetadata(key, WheelOfFortune.getInstance());

        // Setting meta data
        player.setMetadata(key, new FixedMetadataValue(WheelOfFortune.getInstance(), value));
    }

    public static Object getMetaData(final Player player, final String key) {
        return getMetaData(player, key, null);
    }

    public static Object getMetaData(final Player player, final String key, Object defaultValue) {
        for (MetadataValue meta : player.getMetadata(key)) {
            if (meta.getOwningPlugin().getName().equalsIgnoreCase(WheelOfFortune.getInstance().getName())) {
                return meta.value();
            }
        }
        return defaultValue;
    }

    public static String c(final String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String getMessage(final String path) {
        final IConfiguration config = WheelOfFortune.getInstance().getConfiguration();

        return Utils.c(getPrefix() + config.getString("messages." + path));
    }

    public static String prefixedMessage(final String message) {
        return Utils.c(getPrefix() + message);
    }

    public static String getPrefix() {
        return c(WheelOfFortune.getInstance().getConfiguration().getString("messages.prefix"));
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
