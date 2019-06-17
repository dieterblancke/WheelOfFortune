package com.dbsoftwares.dangerwheel.commands;

import com.dbsoftwares.commandapi.command.SpigotCommand;
import com.dbsoftwares.dangerwheel.DangerWheel;
import com.dbsoftwares.dangerwheel.hologram.HologramManager;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class DangerWheelCommand extends SpigotCommand {

    public DangerWheelCommand() {
        super("dangerwheel", Lists.newArrayList("dw"), "dangerwheel.use");
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        return ImmutableList.of();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return ImmutableList.of();
    }

    @Override
    public void onExecute(Player player, String[] args) {
        DangerWheel.getInstance().getScripts().forEach(script -> script.getScript().execute(player));

        new HologramManager(player.getLocation().add(0, 5, 0)).spawnHologram();
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
    }
}
