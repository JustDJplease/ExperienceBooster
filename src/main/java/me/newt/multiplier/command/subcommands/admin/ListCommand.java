package me.newt.multiplier.command.subcommands.admin;

import me.newt.multiplier.command.SubCommand;
import me.newt.multiplier.util.UtilArgumentParsers;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class ListCommand extends SubCommand {

    private final String permission;

    /**
     * Constructor.
     * @param permission Permission required to run this command.
     */
    public ListCommand(String permission) {
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
        // multiplier admin list <player>

        if (!sender.hasPermission(permission)) {
            sender.sendMessage("§cYou do not have permission. (Lacking: " + permission + ")");
            return;
        }

        if (args.length != 3) {
            printInvalidArgs(sender, label);
            return;
        }

        String unValidatedReceiver = args[2];
        UUID uuid = UtilArgumentParsers.parseUUID(unValidatedReceiver);
        if (uuid == null) {
            printInvalidArgs(sender, label);
            return;
        }

        // TODO LIST MULTIPLIERS
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
        sender.sendMessage("§cInvalid arguments! §4/" + label + " admin list <player>");
        sender.sendMessage(" §8* §7<player> §f- (Word) Online player or UUID.");
    }
}
