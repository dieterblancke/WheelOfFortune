package com.dbsoftwares.wheeloffortune.wheel.block;

import com.dbsoftwares.wheeloffortune.WheelOfFortune;
import com.dbsoftwares.wheeloffortune.script.ScriptData;
import com.dbsoftwares.wheeloffortune.utils.Utils;
import com.dbsoftwares.wheeloffortune.utils.objects.CircleColor;
import com.dbsoftwares.wheeloffortune.utils.objects.WheelRun;
import com.dbsoftwares.wheeloffortune.utils.objects.WheelRunData;
import com.dbsoftwares.wheeloffortune.wheel.WheelManager;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

@RequiredArgsConstructor
public class BlockWheelTask extends BukkitRunnable {

    private final WheelManager manager;
    private final BlockCircle circle;
    private final WheelRunData runData;

    @Setter
    private Player executor;

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
        final List<List<Block>> blocks = circle.getCircleBlocks();

        for (int line = 0; line < lines.size(); line++) {
            final List<CircleColor> colorLine = lines.get(line);
            final List<Block> blockLine = blocks.get(line);

            for (int i = 0; i < colorLine.size(); i++) {
                final CircleColor color = colorLine.get(i);
                final Block block = blockLine.get(i);

                block.setType(color.getAsMaterial());
            }
        }
        circle.getCenter().getWorld().playSound(
                circle.getCenter(),
                Sound.valueOf(WheelOfFortune.getInstance().getConfiguration().getString("sounds.tick")),
                15,
                1
        );

        if (runs >= runData.getMaxRuns()) {
            circle.getCenter().getWorld().playSound(
                    circle.getCenter(),
                    Sound.valueOf(WheelOfFortune.getInstance().getConfiguration().getString("sounds.finish")),
                    15,
                    1
            );

            final ScriptData data = circle.getScriptDataFinish();

            if (data == null) {
                Bukkit.broadcastMessage(Utils.getMessage("wheel.no-event"));
            } else {
                Bukkit.broadcastMessage(
                        Utils.getMessage("wheel.event")
                                .replace("{eventName}", data.getName())
                                .replace("{color}", circle.finishColor().beautifiedName())
                );
                data.getScript().execute(executor);
            }
            new BukkitRunnable() {

                @Override
                public void run() {
                    manager.spawnStandard();
                }

            }.runTaskLater(WheelOfFortune.getInstance(), 200);
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
}
