package com.dbsoftwares.dangerwheel.wheel.hologram;

import com.dbsoftwares.dangerwheel.DangerWheel;
import com.dbsoftwares.dangerwheel.script.ScriptData;
import com.dbsoftwares.dangerwheel.utils.Utils;
import com.dbsoftwares.dangerwheel.utils.objects.CircleColor;
import com.dbsoftwares.dangerwheel.utils.objects.WheelRun;
import com.dbsoftwares.dangerwheel.utils.objects.WheelRunData;
import com.dbsoftwares.dangerwheel.wheel.WheelCircle;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

@RequiredArgsConstructor
public class HologramWheelTask extends BukkitRunnable {

    private final Hologram hologram;
    private final WheelCircle circle;
    private final WheelRunData runData;

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

            final ScriptData data = circle.getScriptDataFinish();

            if (data == null) {
                Bukkit.broadcastMessage(Utils.getMessage("wheel.no-event"));
            } else {
                Bukkit.broadcastMessage(Utils.getMessage("wheel.event").replace("{eventName}", data.getName()));
                data.getScript().execute();
            }
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

    private String buildLine(final List<CircleColor> colors) {
        final StringBuilder line = new StringBuilder();

        colors.forEach(color -> line.append(color.getAsText()));

        return line.toString();
    }
}
