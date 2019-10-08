package com.dbsoftwares.wheeloffortune.commands.subcommands;

import com.dbsoftwares.commandapi.command.SubCommand;
import com.dbsoftwares.configuration.api.ISection;
import com.dbsoftwares.wheeloffortune.WheelOfFortune;
import com.dbsoftwares.wheeloffortune.utils.Utils;
import com.dbsoftwares.wheeloffortune.wheel.WheelManager;
import com.google.common.collect.ImmutableList;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class SpinSubCommand extends SubCommand {

    public SpinSubCommand() {
        super("spin", 0);
    }

    @Override
    public String getUsage() {
        return "/fortunewheel spin";
    }

    @Override
    public String getPermission() {
        return "fortunewheel.spin";
    }

    @Override
    public void onExecute(Player player, String[] args) {
        final WheelManager manager = WheelOfFortune.getInstance().getWheelManager();

        if (manager == null) {
            player.sendMessage(Utils.getMessage("wheel.no-wheel"));
            return;
        }
        final int delay = WheelOfFortune.getInstance().getConfiguration().getInteger("wheel.delay");

        new BukkitRunnable() {

            private int runs = delay;

            @Override
            public void run() {
                final ISection section = WheelOfFortune.getInstance().getConfiguration().getSection("messages.wheel.starting");

                if (runs <= 0) {
                    manager.spawn(player);
                    cancel();

                    Bukkit.broadcastMessage(Utils.c(section.getString("starting")));
                } else {
                    String broadcast;

                    if (section.exists(String.valueOf(runs))) {
                        broadcast = Utils.c(section.getString(String.valueOf(runs)));
                    } else {
                        broadcast = Utils.c(section.getString("default"));
                    }
                    if (!broadcast.isEmpty()) {
                        broadcast = broadcast.replace("{timer}", String.valueOf(runs));
                        Bukkit.broadcastMessage(broadcast);
                    }
                }
                runs--;
            }
        }.runTaskTimer(WheelOfFortune.getInstance(), 0, 20);
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