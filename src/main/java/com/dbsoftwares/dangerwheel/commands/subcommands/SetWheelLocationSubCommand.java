package com.dbsoftwares.dangerwheel.commands.subcommands;

import com.dbsoftwares.commandapi.command.SubCommand;
import com.dbsoftwares.configuration.api.IConfiguration;
import com.dbsoftwares.dangerwheel.DangerWheel;
import com.dbsoftwares.dangerwheel.utils.Utils;
import com.google.common.collect.ImmutableList;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.List;

public class SetWheelLocationSubCommand extends SubCommand {

    public SetWheelLocationSubCommand() {
        super("setwheellocation", 0);
    }

    @Override
    public String getUsage() {
        return "/dangerwheel setwheellocation";
    }

    @Override
    public String getPermission() {
        return "dangerwheel.setwheellocation";
    }

    @Override
    public void onExecute(Player player, String[] args) {
        final IConfiguration config = DangerWheel.getInstance().getDataConfig();

        config.set("locations.wheel", player.getLocation());
        try {
            config.save();
            player.sendMessage(Utils.getMessage("admin.location-set"));
        } catch (IOException e) {
            player.sendMessage(Utils.getMessage("admin.error"));
        }
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