package com.dbsoftwares.dangerwheel.hologram;

import com.dbsoftwares.dangerwheel.DangerWheel;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.google.common.collect.Lists;
import org.bukkit.Location;

import java.util.List;

public class HologramManager {

    private final Location location;

    public HologramManager(final Location location) {
        this.location = location;
    }

    public void spawnHologram() {
        final Hologram hologram = HologramsAPI.createHologram(DangerWheel.getInstance(), location);
        final List<String> lines = getCircleLines(15, 15);

        lines.forEach(hologram::appendTextLine);
    }

    private List<String> getCircleLines(final int height, final int width) {
        final float width_r = (float) width / 2;
        final float height_r = (float) height / 2;
        final float ratio = width_r / height_r;

        final double maxblocks_x;
        final double maxblocks_y;

        if ((width_r * 2) % 2 == 0) {
            maxblocks_x = Math.ceil(width_r - .5) * 2 + 1;
        } else {
            maxblocks_x = Math.ceil(width_r) * 2;
        }

        if ((height_r * 2) % 2 == 0) {
            maxblocks_y = Math.ceil(height_r - .5) * 2 + 1;
        } else {
            maxblocks_y = Math.ceil(height_r) * 2;
        }

        final List<String> lines = Lists.newArrayList();

        for (double y = -maxblocks_y / 2 + 1; y <= maxblocks_y / 2 - 1; y++) {
            final StringBuilder line = new StringBuilder();

            for (double x = -maxblocks_x / 2 + 1; x <= maxblocks_x / 2 - 1; x++) {
                if (shouldBeFilled(x, y, width_r, ratio)) {
                    line.append("█");
                }
            }
            lines.add(line.toString());
        }
        return lines;
    }

    private double distance(final double x, double y, double ratio) {
        return Math.sqrt((Math.pow(y * ratio, 2)) + Math.pow(x, 2));
    }

    private boolean shouldBeFilled(final double x, final double y, final double radius, final float ratio) {
        return distance(x, y, ratio) <= radius;
    }
}
