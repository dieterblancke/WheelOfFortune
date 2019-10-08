package com.dbsoftwares.wheeloffortune.wheel.hologram;

import com.dbsoftwares.configuration.api.IConfiguration;
import com.dbsoftwares.configuration.api.ISection;
import com.dbsoftwares.wheeloffortune.WheelOfFortune;
import com.dbsoftwares.wheeloffortune.utils.Utils;
import com.dbsoftwares.wheeloffortune.utils.objects.CircleColor;
import com.dbsoftwares.wheeloffortune.utils.objects.WheelRunData;
import com.dbsoftwares.wheeloffortune.wheel.WheelCircle;
import com.dbsoftwares.wheeloffortune.wheel.WheelManager;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.Random;

public class HologramManager extends WheelManager {

    private final Random random;

    private Hologram hologram;
    private HologramWheelTask runnable;
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

        final IConfiguration config = WheelOfFortune.getInstance().getConfiguration();
        final ISection section = config.getSection("wheel");
        hologram = HologramsAPI.createHologram(WheelOfFortune.getInstance(), location);

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
    public void spawn(final Player executor) {
        runnable.setExecutor(executor);
        task = runnable.runTaskTimer(WheelOfFortune.getInstance(), 0, 1);
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
