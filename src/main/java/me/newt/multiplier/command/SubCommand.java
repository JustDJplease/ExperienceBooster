package me.newt.multiplier.command;

import org.bukkit.command.CommandSender;

public abstract class SubCommand {

    /**
     * Method that is run when this subcommand is executed.
     * @param executor Issuer of the command.
     */
    public abstract void run(CommandSender executor);
}
