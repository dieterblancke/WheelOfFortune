package com.dbsoftwares.dangerwheel.wheel.managers;

import com.dbsoftwares.configuration.api.ISection;
import com.dbsoftwares.dangerwheel.DangerWheel;
import com.dbsoftwares.dangerwheel.utils.objects.CircleColor;
import com.dbsoftwares.dangerwheel.utils.objects.WheelRun;
import com.dbsoftwares.dangerwheel.utils.objects.WheelRunData;
import com.dbsoftwares.dangerwheel.wheel.WheelCircle;
import com.dbsoftwares.dangerwheel.wheel.WheelManager;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;

public class HologramManager extends WheelManager {

    private final Random random;

    public HologramManager(final Location location) {
        super(location);

        this.random = new Random();
    }

    @Override
    public void spawn() {
        final ISection section = DangerWheel.getInstance().getConfiguration().getSection("wheel");

        final Hologram hologram = HologramsAPI.createHologram(DangerWheel.getInstance(), location.get());
        final WheelCircle circle = new WheelCircle(
                section.getInteger("height"),
                section.getInteger("width"),
                section.getInteger("sectors")
        );
        final WheelRunData runData = wheelRuns.get(random.nextInt(wheelRuns.size()));

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
                final List<List<CircleColor>> lines = circle.getLines();

                if (hologram.size() == 0) {
                    lines.forEach(line -> hologram.appendTextLine(buildLine(line)));
                } else {
                    for (int i = 0; i < lines.size(); i++) {
                        final String line = buildLine(lines.get(i));
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

                if (runs >= runData.getMaxRuns()) {
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
                final WheelRun data = runData.getRuns()
                        .stream()
                        .filter(run -> runs >= run.getFirst() && runs < run.getLast())
                        .findFirst()
                        .orElse(null);

                if (data == null) {
                    return runData.getDefaultDuration();
                }
                return data.getTickDuration();
            }

        }.runTaskTimer(DangerWheel.getInstance(), 0, 1);
    }

    private String buildLine(final List<CircleColor> colors) {
        final StringBuilder line = new StringBuilder();

        colors.forEach(color -> line.append(color.getAsText()));

        return line.toString();
    }
}
