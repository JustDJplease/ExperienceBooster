package me.newt.multiplier.command.subcommands.user;

import me.newt.multiplier.MultiplierPlugin;
import me.newt.multiplier.command.SubCommand;
import me.newt.multiplier.messages.MessagesAPI;
import org.bukkit.command.CommandSender;

public class ThankCommand extends SubCommand {

    private final MultiplierPlugin multiplierPlugin;
    private final MessagesAPI msg;
    private final String permission;

    /**
     * Constructor.
     * @param multiplierPlugin Instance of the main class.
     * @param permission       Permission required to run this command.
     */
    public ThankCommand(MultiplierPlugin multiplierPlugin, String permission) {
        this.multiplierPlugin = multiplierPlugin;
        this.msg = multiplierPlugin.getMessagesAPI();
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
        // multiplier thank

        // Right permission?
        if (!sender.hasPermission(permission)) {
            sender.sendMessage(msg.get("command_no_permission", permission));
            return;
        }

        // Right number of arguments?
        if (args.length != 1) {
            sender.sendMessage(msg.fullLine);
            sender.sendMessage(msg.get("command_invalid_arguments"));
            sender.sendMessage(" §7This command has no arguments.");
            sender.sendMessage(msg.fullLine);
            return;
        }

        // TODO Cooldown?
        // Thanking players.
        sender.sendMessage(msg.get("command_thank"));
        multiplierPlugin.getMultiplierAPI().thankAll();
    }

    /**
     * Get the permission string that is required for this command.
     * @return Permission string required to run this command.
     */
    @Override
    public String getPermission() {
        return permission;
    }
}
