package me.newt.multiplier.command.subcommands.admin;

import me.newt.multiplier.command.SubCommand;
import org.bukkit.command.CommandSender;

public class ListCommand extends SubCommand {

    @Override
    public void run(CommandSender executor) {
        // TODO
    }

    @Override
    public String getNeededPermission() {
        return "multiplier.admin.list";
    }
}