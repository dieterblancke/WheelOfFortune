package com.dbsoftwares.wheeloffortune.commands.subcommands;

import com.dbsoftwares.commandapi.command.SubCommand;
import com.dbsoftwares.wheeloffortune.WheelOfFortune;
import com.dbsoftwares.wheeloffortune.utils.Utils;
import com.google.common.collect.ImmutableList;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class ReloadSubCommand extends SubCommand {

    public ReloadSubCommand() {
        super("reload", 0);
    }

    @Override
    public String getUsage() {
        return "/fortunewheel reload";
    }

    @Override
    public String getPermission() {
        return "fortunewheel.reload";
    }

    @Override
    public void onExecute(Player player, String[] args) {
        onExecute((CommandSender) player, args);
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        WheelOfFortune.getInstance().reload();

        sender.sendMessage(Utils.getMessage("admin.reloaded"));
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
