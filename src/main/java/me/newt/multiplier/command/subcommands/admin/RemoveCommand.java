package me.newt.multiplier.command.subcommands.admin;

import me.newt.multiplier.command.SubCommand;
import me.newt.multiplier.util.UtilArgumentParsers;
import org.bukkit.command.CommandSender;

public class RemoveCommand extends SubCommand {

    private final String permission;

    /**
     * Constructor.
     * @param permission Permission required to run this command.
     */
    public RemoveCommand(String permission) {
        this.permission = permission;
    }

    /**
     * Subcommand handler.
     * @param sender Issuer of the command.
     * @param label  Alias of the command used.
     * @param args   Arguments of the command.
     */
    @Override
    public void run(CommandSender sender, String label, String[] args) {
        // multiplier admin remove <id>

        if (!sender.hasPermission(permission)) {
            sender.sendMessage("§cYou do not have permission. (Lacking: " + permission + ")");
            return;
        }

        if (args.length != 3) {
            printInvalidArgs(sender, label);
            return;
        }

        String unValidatedId = args[2];
        Integer id = UtilArgumentParsers.parseInteger(unValidatedId);
        if (id == null) {
            printInvalidArgs(sender, label);
            return;
        }
        if (id < 0) {
            printInvalidArgs(sender, label);
            return;
        }

        // TODO REMOVE ID
        return;
    }

    /**
     * Get the permission string that is required for this command.
     * @return Permission string required to run this command.
     */
    @Override
    public String getPermission() {
        return permission;
    }

    /**
     * Let the CommandSender know that they've entered invalid arguments.
     * @param sender Issuer of the command.
     * @param label  Alias of the command used.
     */
    private void printInvalidArgs(CommandSender sender, String label) {
        sender.sendMessage("§cInvalid arguments! §4/" + label + " admin remove <id>");
        sender.sendMessage(" §8* §7<id> §f- (Number) ID of a multiplier.");
        sender.sendMessage(" §8* §7§oUse the 'list' command to figure out what ID to use.");
    }
}
