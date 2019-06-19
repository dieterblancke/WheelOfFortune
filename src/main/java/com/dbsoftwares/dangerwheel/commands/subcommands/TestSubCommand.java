package com.dbsoftwares.dangerwheel.commands.subcommands;

import com.dbsoftwares.commandapi.command.SubCommand;
import com.dbsoftwares.dangerwheel.DangerWheel;
import com.dbsoftwares.dangerwheel.hologram.HologramManager;
import com.dbsoftwares.dangerwheel.utils.Utils;
import com.google.common.collect.ImmutableList;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class TestSubCommand extends SubCommand {

    public TestSubCommand() {
        super("test", 0);
    }

    @Override
    public String getUsage() {
        return "/dangerwheel test";
    }

    @Override
    public String getPermission() {
        return "dangerwheel.test";
    }

    @Override
    public void onExecute(Player player, String[] args) {
        DangerWheel.getInstance().getScripts().forEach(script -> script.getScript().execute(player));

        new HologramManager(player.getLocation().add(0, 5, 0)).spawnHologram();
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        sender.sendMessage(Utils.getMessage("not-for-console"));
    }

    @Override
    public List<String> getCompletions(CommandSender sender, String[] args) {
        return ImmutableList.of();
    }

    @Override
    public List<String> getCompletions(Player player, String[] args) {
        return ImmutableList.of();
    }
}
