package com.dbsoftwares.wheeloffortune.wheel.block;

import com.dbsoftwares.wheeloffortune.wheel.WheelCircle;
import com.google.common.collect.Lists;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.List;

public class BlockCircle extends WheelCircle {

    @Getter
    private Location center;

    public BlockCircle(final Location center, final int height, final int width, final int sectors) {
        super(height, width, sectors);

        this.center = center;
    }

    public List<List<Block>> getCircleBlocks() {
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

        final List<List<Block>> blocks = Lists.newArrayList();

        for (double y = -maxblocks_y / 2 + 1; y <= maxblocks_y / 2 - 1; y++) {
            final List<Block> line = Lists.newArrayList();

            for (double x = -maxblocks_x / 2 + 1; x <= maxblocks_x / 2 - 1; x++) {
                if (shouldBeFilled(x, y, width_r, ratio)) {
                    line.add(center.clone().add(x, 0, y).getBlock());
                }
            }
            blocks.add(line);
        }
        return blocks;
    }
}
