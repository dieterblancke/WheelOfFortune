package com.dbsoftwares.dangerwheel.hologram;

import com.dbsoftwares.dangerwheel.DangerWheel;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

public class HologramManager {


    public void spawnHologram() {
        final Hologram hologram = HologramsAPI.createHologram(DangerWheel.getInstance(), null);

        // TODO: build hologram
    }

}
