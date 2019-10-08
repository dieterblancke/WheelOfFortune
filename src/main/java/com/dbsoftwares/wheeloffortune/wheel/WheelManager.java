package com.dbsoftwares.wheeloffortune.wheel;

import com.dbsoftwares.wheeloffortune.WheelOfFortune;
import com.dbsoftwares.wheeloffortune.utils.objects.WheelRun;
import com.dbsoftwares.wheeloffortune.utils.objects.WheelRunData;
import com.google.common.collect.Lists;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class WheelManager {

    protected final Location location;
    protected List<WheelRunData> wheelRuns = Lists.newArrayList();

    public WheelManager(final Location location) {
        this.location = location;

        this.loadWheelRuns();
    }

    private void loadWheelRuns() {
        WheelOfFortune.getInstance().getConfiguration().getSectionList("wheel.runs").forEach(runData -> {
            final int maxRuns = runData.getInteger("max-runs");
            final int defaultDuration = runData.getInteger("default-tick-duration");
            final WheelRunData data = new WheelRunData(maxRuns, defaultDuration, Lists.newArrayList());

            runData.getSectionList("runs").forEach(run -> {
                final int first = run.getInteger("first");
                final int last = run.getInteger("last");
                final int tickDuration = run.getInteger("tick-duration");

                data.getRuns().add(new WheelRun(first, last, tickDuration));
            });

            wheelRuns.add(data);
        });
    }

    public abstract void spawnStandard();

    public abstract void spawn(Player executor);

    public abstract void despawn();
}
