package me.newt.multiplier.command.subcommands.user;

import me.newt.multiplier.MultiplierPlugin;
import me.newt.multiplier.command.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class MenuCommand extends SubCommand {

    private final MultiplierPlugin multiplierPlugin;
    private final String permission;

    /**
     * Constructor.
     * @param multiplierPlugin Instance of the main class.
     * @param permission       Permission required to run this command.
     */
    public MenuCommand(MultiplierPlugin multiplierPlugin, String permission) {
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
        // multiplier

        if (!sender.hasPermission(permission)) {
            sender.sendMessage("§cYou do not have permission. (Lacking: " + permission + ")");
            return;
        }

        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();

        // TODO OPEN GUI
        // TODO THIS IS TEMPORARY:
        sender.sendMessage("§7Listing multipliers:");
        multiplierPlugin.getMultiplierAPI().getMultipliers(uuid).forEach(multiplier -> sender.sendMessage(multiplier.getMultiplierAsText()));
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
