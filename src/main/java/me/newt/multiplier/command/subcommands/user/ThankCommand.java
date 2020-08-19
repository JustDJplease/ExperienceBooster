package me.newt.multiplier.command.subcommands.user;

import me.newt.multiplier.command.SubCommand;
import org.bukkit.command.CommandSender;

public class ThankCommand extends SubCommand {

    private final String permission;

    /**
     * Constructor.
     * @param permission Permission required to run this command.
     */
    public ThankCommand(String permission) {
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

        if (!sender.hasPermission(permission)) {
            sender.sendMessage("§cYou do not have permission. (Lacking: " + permission + ")");
            return;
        }

        // TODO THANK ACTIVATORS
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
}
