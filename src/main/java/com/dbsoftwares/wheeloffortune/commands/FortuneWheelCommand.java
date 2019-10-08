package com.dbsoftwares.wheeloffortune.commands;

import com.dbsoftwares.commandapi.command.MainSpigotCommand;
import com.dbsoftwares.wheeloffortune.commands.subcommands.*;
import com.google.common.collect.Lists;

public class FortuneWheelCommand extends MainSpigotCommand {

    public FortuneWheelCommand() {
        super("fortunewheel", Lists.newArrayList("fw", "wof", "wheeloffortune"), "fortunewheel.use");

        subCommands.add(new ReloadSubCommand());
        subCommands.add(new TestSubCommand());
        subCommands.add(new SetWheelLocationSubCommand());
        subCommands.add(new SpinSubCommand());
        subCommands.add(new OptInSubCommand());
        subCommands.add(new OptOutSubCommand());
    }
}
