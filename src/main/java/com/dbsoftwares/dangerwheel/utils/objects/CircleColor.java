package com.dbsoftwares.dangerwheel.utils.objects;

import com.dbsoftwares.dangerwheel.DangerWheel;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;

public enum CircleColor {

    BLACK("BLACK"),
    DARK_BLUE("BLUE"),
    DARK_GREEN("GREEN"),
    DARK_RED("RED"),
    DARK_PURPLE("PURPLE"),
    GOLD("ORANGE"),
    GRAY("LIGHT_GRAY"),
    DARK_GRAY("GRAY"),
    BLUE("LIGHT_BLUE"),
    GREEN("LIME"),
    AQUA("CYAN"),
    RED("PINK"),
    LIGHT_PURPLE("MAGENTA"),
    YELLOW("YELLOW"),
    WHITE("WHITE");

    private String materialColorName;

    CircleColor(final String materialColorName) {
        this.materialColorName = materialColorName;
    }

    public ChatColor getChatColor() {
        return ChatColor.valueOf(toString());
    }

    public String getAsText() {
        return this.getChatColor().toString() + DangerWheel.getInstance().getConfiguration().getString("wheel.hologram.icon");
    }

    public Material getAsMaterial() {
        return Material.valueOf(
                this.materialColorName + "_" + DangerWheel.getInstance().getConfiguration().getString("wheel.block.type")
        );
    }
}
