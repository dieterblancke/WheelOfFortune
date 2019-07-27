package com.dbsoftwares.dangerwheel.api;

import com.dbsoftwares.dangerwheel.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.stream.Collectors;

public class DWApi {

    private static final DWApi instance = new DWApi();

    private DWApi() {
    }

    public static DWApi getInstance() {
        return instance;
    }

    public Collection<Player> getOptedInOnlinePlayers() {
        return Bukkit.getOnlinePlayers().stream()
                .filter(p -> (boolean) Utils.getMetaData(p, Utils.OPTED_IN_KEY, true))
                .collect(Collectors.toList());
    }
}
