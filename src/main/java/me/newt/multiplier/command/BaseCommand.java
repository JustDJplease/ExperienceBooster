package me.newt.multiplier.command;

import me.newt.multiplier.MultiplierPlugin;
import me.newt.multiplier.command.subcommands.admin.*;
import me.newt.multiplier.command.subcommands.user.MenuCommand;
import me.newt.multiplier.command.subcommands.user.ThankCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BaseCommand implements CommandExecutor {

    private final MultiplierPlugin multiplierPlugin;
    private final SubCommand userMenuCommand;
    private final SubCommand userThankCommand;
    private final SubCommand adminGiveCommand;
    private final SubCommand adminListCommand;
    private final SubCommand adminReloadCommand;
    private final SubCommand adminRemoveCommand;
    private final SubCommand adminStartCommand;
    private final SubCommand adminStopCommand;

    /**
     * Constructor.
     * @param multiplierPlugin Instance of the main class.
     */
    public BaseCommand(MultiplierPlugin multiplierPlugin) {
        this.multiplierPlugin = multiplierPlugin;

        // User subcommands:
        this.userMenuCommand = new MenuCommand();
        this.userThankCommand = new ThankCommand();

        // Admin subcommands:
        this.adminGiveCommand = new GiveCommand();
        this.adminListCommand = new ListCommand();
        this.adminReloadCommand = new ReloadCommand();
        this.adminRemoveCommand = new RemoveCommand();
        this.adminStartCommand = new StartCommand();
        this.adminStopCommand = new StopCommand();
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
        if (args.length == 0) {
            if (!(commandSender instanceof Player)) {
                printHelp(commandSender, label);
                return true;
            }
            if (!hasPermission(commandSender, "multiplier.user")) {
                return true;
            }
            commandSender.sendMessage("[TODO] Should open GUI.");
            return true;
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("thank")) {
                commandSender.sendMessage("[TODO] Thank players.");
                return true;
            }
            printHelp(commandSender, label);
            return true;
        }
        if (args.length > 2) {
            if (args[0].equalsIgnoreCase("admin")) {
                if (args[1].equalsIgnoreCase("give")) {

                }
                if (args[1].equalsIgnoreCase("list")) {

                }
                if (args[1].equalsIgnoreCase("remove")) {

                }
                if (args[1].equalsIgnoreCase("start")) {

                }
                if (args[1].equalsIgnoreCase("stop")) {

                }
                if (args[1].equalsIgnoreCase("reload")) {

                }
            }
        }
        printHelp(commandSender, label);
        return true;
    }

    /*
     * USER COMMANDS:
     * /multiplier              Opens multiplier gui.
     * /multiplier help         Shows multiplier help.
     * /multiplier thank        Thank the players that activated a multiplier.
     *
     * ADMIN COMMANDS:
     * /multiplier admin give <player/uuid> <type> <duration> <strength>    Give a multiplier.
     * /multiplier admin list <player/uuid>                                 List a player's multipliers.
     * /multiplier admin remove <id>                                        Remove a multiplier.
     * /multiplier admin start <type> <duration> <strength>                 Start a multiplier.
     * /multiplier admin stop <type>                                        Stop all active multipliers for that type.
     * /multiplier admin reload                                             Reload the configuration files.
     */

    /**
     * Send command help to the issuer of a command.
     * @param commandSender Issuer of the command.
     * @param label         Alias of the command used.
     */
    private void printHelp(CommandSender commandSender, String label) {
        commandSender.sendMessage("[TODO] User commands:");
        commandSender.sendMessage("/" + label + " - Opens multiplier menu.");
        commandSender.sendMessage("/" + label + " thank - Thank players for activating multiplier.");
        commandSender.sendMessage("/" + label + " help - Shows this help menu.");
        commandSender.sendMessage("[TODO] Admin commands:");
        commandSender.sendMessage("/" + label + " admin give <player> <type> <duration> <strength> - Give multiplier to player.");
        commandSender.sendMessage("/" + label + " admin list <player> - List player's multipliers.");
        commandSender.sendMessage("/" + label + " admin remove <id> - Remove multiplier from player.");
        commandSender.sendMessage("/" + label + " admin start <type> <duration> <strength> - Start multiplier.");
        commandSender.sendMessage("/" + label + " admin stop <type> - Stop multiplier.");
        commandSender.sendMessage("/" + label + " admin reload - Reload configuration files.");
    }

    private boolean hasPermission(CommandSender commandSender, String permission) {
        if (commandSender.hasPermission(permission)) {
            return true;
        }
        commandSender.sendMessage("§cYou do not have permission. (Lacking: " + permission + ")");
        return false;
    }
}
