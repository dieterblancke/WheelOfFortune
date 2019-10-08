package com.dbsoftwares.wheeloffortune.commands.subcommands;

import com.dbsoftwares.commandapi.command.SubCommand;
import com.dbsoftwares.configuration.api.ISection;
import com.dbsoftwares.wheeloffortune.WheelOfFortune;
import com.dbsoftwares.wheeloffortune.utils.Utils;
import com.dbsoftwares.wheeloffortune.wheel.WheelManager;
import com.dbsoftwares.wheeloffortune.wheel.hologram.HologramManager;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class TestSubCommand extends SubCommand {

    @Getter
    private static final List<WheelManager> managers = Lists.newArrayList();

    public TestSubCommand() {
        super("test", 0);
    }

    @Override
    public String getUsage() {
        return "/fortunewheel test";
    }

    @Override
    public String getPermission() {
        return "fortunewheel.test";
    }

    @Override
    public void onExecute(Player player, String[] args) {
        final Location location = player.getLocation().add(0, 5, 0);
        final int delay = WheelOfFortune.getInstance().getConfiguration().getInteger("wheel.delay");

        new BukkitRunnable() {

            private int runs = delay;

            @Override
            public void run() {
                final ISection section = WheelOfFortune.getInstance().getConfiguration().getSection("messages.wheel.starting");

                if (runs <= 0) {
                    final WheelManager manager = new HologramManager(location);
                    manager.spawnStandard();
                    manager.spawn(player);
                    cancel();

                    Bukkit.broadcastMessage(Utils.c(section.getString("starting")));
                    managers.add(manager);
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
