package com.dbsoftwares.dangerwheel.commands;

import com.dbsoftwares.commandapi.command.MainSpigotCommand;
import com.dbsoftwares.dangerwheel.commands.subcommands.ReloadSubCommand;
import com.dbsoftwares.dangerwheel.commands.subcommands.TestSubCommand;
import com.google.common.collect.Lists;

public class DangerWheelCommand extends MainSpigotCommand {

    public DangerWheelCommand() {
        super("dangerwheel", Lists.newArrayList("dw"), "dangerwheel.use");

        subCommands.add(new ReloadSubCommand());
        subCommands.add(new TestSubCommand());
    }
}
