package com.dbsoftwares.dangerwheel.wheel.block;

import com.dbsoftwares.configuration.api.IConfiguration;
import com.dbsoftwares.configuration.api.ISection;
import com.dbsoftwares.dangerwheel.DangerWheel;
import com.dbsoftwares.dangerwheel.utils.objects.CircleColor;
import com.dbsoftwares.dangerwheel.utils.objects.WheelRunData;
import com.dbsoftwares.dangerwheel.wheel.WheelManager;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Random;

public class BlockManager extends WheelManager {

    private final Random random;

    private BukkitRunnable runnable;
    private BukkitTask task;

    public BlockManager(final Location location) {
        super(location);

        this.random = new Random();
    }

    @Override
    public void spawnStandard() {
        final IConfiguration config = DangerWheel.getInstance().getConfiguration();
        final ISection section = config.getSection("wheel");

        final BlockCircle blockCircle = new BlockCircle(
                location,
                section.getInteger("height"),
                section.getInteger("width"),
                section.getInteger("sectors")
        );

        blockCircle.getCircleBlocks().forEach(line -> line.forEach(block -> block.setType(CircleColor.WHITE.getAsMaterial())));

        final WheelRunData runData = wheelRuns.get(random.nextInt(wheelRuns.size()));
        this.runnable = new BlockWheelTask(this, blockCircle, runData);
    }

    @Override
    public void spawn() {
        task = runnable.runTaskTimer(DangerWheel.getInstance(), 0, 1);
    }

    @Override
    public void despawn() {
        if (task != null && !task.isCancelled()) {
            task.cancel();

            spawnStandard();
        }
    }
}
