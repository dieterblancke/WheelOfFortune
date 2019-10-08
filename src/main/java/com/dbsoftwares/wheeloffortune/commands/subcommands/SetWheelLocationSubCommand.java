package com.dbsoftwares.wheeloffortune.commands.subcommands;

import com.dbsoftwares.commandapi.command.SubCommand;
import com.dbsoftwares.configuration.api.IConfiguration;
import com.dbsoftwares.wheeloffortune.WheelOfFortune;
import com.dbsoftwares.wheeloffortune.utils.Utils;
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
        return "/fortunewheel setwheellocation";
    }

    @Override
    public String getPermission() {
        return "fortunewheel.setwheellocation";
    }

    @Override
    public void onExecute(Player player, String[] args) {
        final IConfiguration config = WheelOfFortune.getInstance().getDataConfig();

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