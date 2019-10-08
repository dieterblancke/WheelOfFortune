package com.dbsoftwares.wheeloffortune.wheel.block;

import com.dbsoftwares.configuration.api.IConfiguration;
import com.dbsoftwares.configuration.api.ISection;
import com.dbsoftwares.wheeloffortune.WheelOfFortune;
import com.dbsoftwares.wheeloffortune.utils.objects.CircleColor;
import com.dbsoftwares.wheeloffortune.utils.objects.WheelRunData;
import com.dbsoftwares.wheeloffortune.wheel.WheelManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.Random;

public class BlockManager extends WheelManager {

    private final Random random;

    private BlockWheelTask runnable;
    private BukkitTask task;

    public BlockManager(final Location location) {
        super(location);

        this.random = new Random();
    }

    @Override
    public void spawnStandard() {
        final IConfiguration config = WheelOfFortune.getInstance().getConfiguration();
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
    public void spawn(final Player executor) {
        runnable.setExecutor(executor);
        task = runnable.runTaskTimer(WheelOfFortune.getInstance(), 0, 1);
    }

    @Override
    public void despawn() {
        if (task != null && !task.isCancelled()) {
            task.cancel();

            spawnStandard();
        }
    }
}
