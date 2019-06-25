package com.dbsoftwares.dangerwheel.wheel;

import com.dbsoftwares.dangerwheel.DangerWheel;
import com.dbsoftwares.dangerwheel.script.ScriptData;
import com.dbsoftwares.dangerwheel.utils.objects.CircleColor;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

public class WheelCircle {

    private final List<CircleColor> colors;
    private final int height;
    private final int width;
    private final int sectors;

    public WheelCircle(final int height, final int width, final int sectors) {
        if (sectors > CircleColor.values().length) {
            throw new RuntimeException("There cannot be more sectors then colors ...");
        }
        final List<CircleColor> colorsCopy = Lists.newArrayList(CircleColor.values());
        Collections.shuffle(colorsCopy);

        this.colors = colorsCopy.subList(0, sectors);

        this.height = height;
        this.width = width;
        this.sectors = sectors;
    }

    public void moveColors() {
        Collections.rotate(colors, 1);
    }

    public List<List<CircleColor>> getLines() {
        final float width_r = (float) width / 2;
        final float height_r = (float) height / 2;
        final float ratio = width_r / height_r;

        final double maxblocks_x;
        final double maxblocks_y;

        if ((width_r * 2) % 2 == 0) {
            maxblocks_x = Math.ceil(width_r - .5) * 2 + 1;
        } else {
            maxblocks_x = Math.ceil(width_r) * 2;
        }

        if ((height_r * 2) % 2 == 0) {
            maxblocks_y = Math.ceil(height_r - .5) * 2 + 1;
        } else {
            maxblocks_y = Math.ceil(height_r) * 2;
        }

        final List<List<CircleColor>> lines = Lists.newArrayList();

        for (double y = -maxblocks_y / 2 + 1; y <= maxblocks_y / 2 - 1; y++) {
            final List<CircleColor> line = Lists.newArrayList();

            for (double x = -maxblocks_x / 2 + 1; x <= maxblocks_x / 2 - 1; x++) {
                if (shouldBeFilled(x, y, width_r, ratio)) {
                    final int sector = getSectorNumber(x, y);

                    line.add(colors.get(sector));
                }
            }
            lines.add(line);
        }
        return lines;
    }

    private int getSectorNumber(final double x, final double y) {
        final double degreesPerSector = 360 / sectors;
        final double rad = Math.atan2(y, x);
        final double degrees = rad * (180 / Math.PI) + 180;

        for (int i = 0; i < sectors; i++) {
            final double startDegrees = degreesPerSector * i;
            final double endDegrees = startDegrees + degreesPerSector;

            if (degrees >= startDegrees && degrees <= endDegrees) {
                return i;
            }
        }

        return -1;
    }

    private double distance(final double x, final double y, final double ratio) {
        return Math.sqrt((Math.pow(y * ratio, 2)) + Math.pow(x, 2));
    }

    private boolean shouldBeFilled(final double x, final double y, final double radius, final float ratio) {
        return distance(x, y, ratio) <= radius;
    }

    public ScriptData getScriptDataFinish() {
        final CircleColor color = this.colors.get(2);

        return DangerWheel.getInstance().getColorEvents().getOrDefault(color, null);
    }
}
