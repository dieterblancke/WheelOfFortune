package com.dbsoftwares.dangerwheel.hologram;

import com.dbsoftwares.configuration.api.ISection;
import com.dbsoftwares.dangerwheel.DangerWheel;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import org.bukkit.Location;

import java.lang.ref.WeakReference;
import java.util.List;

public class HologramManager {

    private final WeakReference<Location> location;

    public HologramManager(final Location location) {
        this.location = new WeakReference<>(location);
    }

    public void spawnHologram() {
        final ISection section = DangerWheel.getInstance().getConfiguration().getSection("hologram.wheel");

        final Hologram hologram = HologramsAPI.createHologram(DangerWheel.getInstance(), location.get());
        final HologramCircle circle = new HologramCircle(
                section.getInteger("height"),
                section.getInteger("width"),
                section.getInteger("sectors")
        );
        final List<String> lines = circle.getLines();

        lines.forEach(hologram::appendTextLine);
    }
}
