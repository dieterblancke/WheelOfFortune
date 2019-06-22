package com.dbsoftwares.dangerwheel.wheel;

import com.dbsoftwares.dangerwheel.DangerWheel;
import com.dbsoftwares.dangerwheel.utils.objects.WheelRun;
import com.dbsoftwares.dangerwheel.utils.objects.WheelRunData;
import com.google.common.collect.Lists;
import org.bukkit.Location;

import java.lang.ref.WeakReference;
import java.util.List;

public abstract class WheelManager {

    protected final WeakReference<Location> location;
    protected List<WheelRunData> wheelRuns = Lists.newArrayList();

    public WheelManager(final Location location) {
        this.location = new WeakReference<>(location);

        this.loadWheelRuns();
    }

    private void loadWheelRuns() {
        DangerWheel.getInstance().getConfiguration().getSectionList("wheel.runs").forEach(runData -> {
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

    public abstract void spawn();
}