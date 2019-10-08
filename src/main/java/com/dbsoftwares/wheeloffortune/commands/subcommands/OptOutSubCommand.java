package com.dbsoftwares.wheeloffortune.commands.subcommands;

import com.dbsoftwares.commandapi.command.SubCommand;
import com.dbsoftwares.wheeloffortune.WheelOfFortune;
import com.dbsoftwares.wheeloffortune.utils.Utils;
import com.google.common.collect.ImmutableList;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class OptOutSubCommand extends SubCommand {

    public OptOutSubCommand() {
        super("optout", 0);
    }

    @Override
    public String getUsage() {
        return "/fortunewheel opt-out";
    }

    @Override
    public String getPermission() {
        return "fortunewheel.optout";
    }

    @Override
    public void onExecute(Player player, String[] args) {
        Utils.setMetaData(player, Utils.OPTED_IN_KEY, false);
        WheelOfFortune.getInstance().getStorage().optOut(player.getUniqueId());

        player.sendMessage(Utils.getMessage("optout"));
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        // do nothing
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
