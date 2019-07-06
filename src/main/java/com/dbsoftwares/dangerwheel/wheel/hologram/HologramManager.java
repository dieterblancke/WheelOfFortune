package com.dbsoftwares.dangerwheel.wheel.hologram;

import com.dbsoftwares.configuration.api.IConfiguration;
import com.dbsoftwares.configuration.api.ISection;
import com.dbsoftwares.dangerwheel.DangerWheel;
import com.dbsoftwares.dangerwheel.utils.Utils;
import com.dbsoftwares.dangerwheel.utils.objects.CircleColor;
import com.dbsoftwares.dangerwheel.utils.objects.WheelRunData;
import com.dbsoftwares.dangerwheel.wheel.WheelCircle;
import com.dbsoftwares.dangerwheel.wheel.WheelManager;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.Random;

public class HologramManager extends WheelManager {

    private final Random random;

    private Hologram hologram;
    private BukkitRunnable runnable;
    private BukkitTask task;

    public HologramManager(final Location location) {
        super(location);

        this.random = new Random();
    }

    @Override
    public void spawnStandard() {
        if (hologram != null && !hologram.isDeleted()) {
            hologram.delete();
        }

        final IConfiguration config = DangerWheel.getInstance().getConfiguration();
        final ISection section = config.getSection("wheel");
        hologram = HologramsAPI.createHologram(DangerWheel.getInstance(), location);

        final WheelCircle wheelCircle = new WheelCircle(
                section.getInteger("height"),
                section.getInteger("width"),
                section.getInteger("sectors")
        );

        final List<String> header = config.getStringList("wheel.hologram.header");
        final List<String> footer = config.getStringList("wheel.hologram.footer");

        header.forEach(line -> hologram.appendTextLine(Utils.c(line)));
        wheelCircle.getBlankLines().forEach(line -> hologram.appendTextLine(buildLine(line)));
        footer.forEach(line -> hologram.appendTextLine(Utils.c(line)));

        final WheelRunData runData = wheelRuns.get(random.nextInt(wheelRuns.size()));
        this.runnable = new HologramWheelTask(this, hologram, wheelCircle, runData, header.size());
    }

    @Override
    public void spawn() {
        task = runnable.runTaskTimer(DangerWheel.getInstance(), 0, 1);
    }

    @Override
    public void despawn() {
        if (task != null && !task.isCancelled()) {
            task.cancel();
        }
        hologram.delete();
    }

    private String buildLine(final List<CircleColor> colors) {
        final StringBuilder line = new StringBuilder();

        colors.forEach(color -> line.append(color.getAsText()));

        return line.toString();
    }
}
