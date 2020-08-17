package me.newt.multiplier.command.subcommands.user;

import me.newt.multiplier.command.SubCommand;
import org.bukkit.command.CommandSender;

public class MenuCommand extends SubCommand {

    @Override
    public void run(CommandSender executor) {
        // TODO
    }

    @Override
    public String getNeededPermission() {
        return "multiplier.user.activate";
    }
}
