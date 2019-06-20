package com.dbsoftwares.dangerwheel.hologram;

import com.dbsoftwares.configuration.api.ISection;
import com.dbsoftwares.dangerwheel.DangerWheel;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

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

        new BukkitRunnable() {

            private int ticks = 0;
            private int runs = 0;

            @Override
            public void run() {
                if (ticks < calculateRequiredTimeForRun()) {
                    ticks++;
                    return;
                }

                ticks = 0;
                runs++;

                circle.moveColors();
                final List<String> lines = circle.getLines();

                if (hologram.size() == 0) {
                    lines.forEach(hologram::appendTextLine);
                } else {
                    for (int i = 0; i < lines.size(); i++) {
                        final String line = lines.get(i);
                        final TextLine textLine = (TextLine) hologram.getLine(i);

                        textLine.setText(line);
                    }
                }
                hologram.getWorld().playSound(
                        hologram.getLocation(),
                        Sound.valueOf(DangerWheel.getInstance().getConfiguration().getString("sounds.tick")),
                        15,
                        1
                );

                if (runs >= 50) {
                    hologram.getWorld().playSound(
                            hologram.getLocation(),
                            Sound.valueOf(DangerWheel.getInstance().getConfiguration().getString("sounds.finish")),
                            15,
                            1
                    );

                    System.out.println("Top sector: " + circle.getCurrentTopSector());
                    cancel();
                }
            }

            private int calculateRequiredTimeForRun() {
                if (runs < 10) {
                    return 4;
                } else if (runs < 20) {
                    return 6;
                } else if (runs < 35) {
                    return 12;
                } else if (runs < 44) {
                    return 18;
                } else if (runs < 47) {
                    return 25;
                } else {
                    return 30;
                }
            }
        }.runTaskTimer(DangerWheel.getInstance(), 0, 1);
    }
}
