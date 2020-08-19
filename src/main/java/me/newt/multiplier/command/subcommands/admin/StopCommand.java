package me.newt.multiplier.command.subcommands.admin;

import me.newt.multiplier.MultiplierType;
import me.newt.multiplier.command.SubCommand;
import me.newt.multiplier.util.UtilArgumentParsers;
import org.bukkit.command.CommandSender;

public class StopCommand extends SubCommand {

    private final String permission;

    /**
     * Constructor.
     * @param permission Permission required to run this command.
     */
    public StopCommand(String permission) {
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
        // multiplier admin stop <type>

        if (!sender.hasPermission(permission)) {
            sender.sendMessage("§cYou do not have permission. (Lacking: " + permission + ")");
            return;
        }

        if (args.length != 3) {
            printInvalidArgs(sender, label);
            return;
        }

        String unValidatedType = args[2];

        MultiplierType type = UtilArgumentParsers.parseType(unValidatedType);
        if (type == null) {
            printInvalidArgs(sender, label);
            return;
        }

        // TODO STOP MULTIPLIER
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
        sender.sendMessage("§cInvalid arguments! §4/" + label + " admin stop <type>");
        sender.sendMessage(" §8* §7<type> §f- (Word) Multiplier type.");
    }
}
