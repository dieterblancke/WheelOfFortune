package com.dbsoftwares.dangerwheel.wheel.hologram;

import com.dbsoftwares.configuration.api.ISection;
import com.dbsoftwares.dangerwheel.DangerWheel;
import com.dbsoftwares.dangerwheel.utils.objects.WheelRunData;
import com.dbsoftwares.dangerwheel.wheel.WheelCircle;
import com.dbsoftwares.dangerwheel.wheel.WheelManager;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

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

        final BukkitRunnable task = new HologramWheelTask(hologram, circle, runData);
        task.runTaskTimer(DangerWheel.getInstance(), 0, 1);
    }
}
