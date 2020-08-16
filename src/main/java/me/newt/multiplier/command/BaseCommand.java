package me.newt.multiplier.command;

import me.newt.multiplier.MultiplierPlugin;
import me.newt.multiplier.command.subcommands.AdminCommand;
import me.newt.multiplier.command.subcommands.UserCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BaseCommand implements CommandExecutor {

    private MultiplierPlugin multiplierPlugin;
    private SubCommand userCommand;
    private SubCommand adminCommand;

    /**
     * Constructor.
     * @param multiplierPlugin Instance of the main class.
     */
    public BaseCommand(MultiplierPlugin multiplierPlugin) {
        this.multiplierPlugin = multiplierPlugin;
        this.userCommand = new UserCommand();
        this.adminCommand = new AdminCommand();
    }

    /**
     * Command handler.
     * @param commandSender Issuer of the command.
     * @param command       Executed command.
     * @param label         Alias of the command used.
     * @param args          Arguments used with the command.
     * @return Whether the command was successfully executed (Always true).
     */
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if(args.length > 0){
            if(args[0].equalsIgnoreCase("admin")){
                adminCommand.run(commandSender);
            }
        }
        // TODO
        return true;
    }

    /**
     * Send command help to the issuer of a command.
     * @param commandSender Issuer of the command.
     * @param label         Alias of the command used.
     */
    private void printHelp(CommandSender commandSender, String label) {
        // TODO
    }
}
