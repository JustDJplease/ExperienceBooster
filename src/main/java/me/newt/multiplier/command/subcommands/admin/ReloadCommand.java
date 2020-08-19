package me.newt.multiplier.command.subcommands.admin;

import me.newt.multiplier.MultiplierPlugin;
import me.newt.multiplier.command.SubCommand;
import org.bukkit.command.CommandSender;

public class ReloadCommand extends SubCommand {

    private final MultiplierPlugin multiplierPlugin;
    private final String permission;

    /**
     * Constructor.
     * @param multiplierPlugin Instance of the main class.
     * @param permission Permission required to run this command.
     */
    public ReloadCommand(MultiplierPlugin multiplierPlugin, String permission) {
        this.multiplierPlugin = multiplierPlugin;
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
        // multiplier admin reload
        if (args.length != 2) {
            printInvalidArgs(sender, label);
            return;
        }

        sender.sendMessage("§7Reloading configuration files...");
        // TODO RELOAD PLUGIN
        sender.sendMessage("§7Reloaded!");
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
        sender.sendMessage("§cInvalid arguments! §4/" + label + " admin reload");
    }
}
