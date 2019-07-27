package com.dbsoftwares.dangerwheel.commands;

import com.dbsoftwares.commandapi.command.MainSpigotCommand;
import com.dbsoftwares.dangerwheel.commands.subcommands.*;
import com.google.common.collect.Lists;

public class DangerWheelCommand extends MainSpigotCommand {

    public DangerWheelCommand() {
        super("dangerwheel", Lists.newArrayList("dw"), "dangerwheel.use");

        subCommands.add(new ReloadSubCommand());
        subCommands.add(new TestSubCommand());
        subCommands.add(new SetWheelLocationSubCommand());
        subCommands.add(new SpinSubCommand());
        subCommands.add(new OptInSubCommand());
        subCommands.add(new OptOutSubCommand());
    }
}
