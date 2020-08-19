package me.newt.multiplier.command.subcommands.admin;

import me.newt.multiplier.Multiplier;
import me.newt.multiplier.MultiplierPlugin;
import me.newt.multiplier.MultiplierType;
import me.newt.multiplier.command.SubCommand;
import me.newt.multiplier.util.UtilArgumentParsers;
import org.bukkit.command.CommandSender;

public class StartCommand extends SubCommand {

    private final MultiplierPlugin multiplierPlugin;
    private final String permission;

    /**
     * Constructor.
     * @param multiplierPlugin Instance of the main class.
     * @param permission       Permission required to run this command.
     */
    public StartCommand(MultiplierPlugin multiplierPlugin, String permission) {
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
        // multiplier admin start <type> <duration> <strength>

        if (!sender.hasPermission(permission)) {
            sender.sendMessage("§cYou do not have permission. (Lacking: " + permission + ")");
            return;
        }

        if (args.length != 5) {
            printInvalidArgs(sender, label);
            return;
        }

        String unValidatedType = args[2];
        String unValidatedDuration = args[3];
        String unValidatedStrength = args[4];

        MultiplierType type = UtilArgumentParsers.parseType(unValidatedType);
        if (type == null) {
            printInvalidArgs(sender, label);
            return;
        }

        Integer duration = UtilArgumentParsers.parseInteger(unValidatedDuration);
        if (duration == null) {
            printInvalidArgs(sender, label);
            return;
        }

        Integer strength = UtilArgumentParsers.parseInteger(unValidatedStrength);
        if (strength == null) {
            printInvalidArgs(sender, label);
            return;
        }

        sender.sendMessage("§7Starting a §f" + type.getCapitalizedName() + " §7multiplier.");
        Multiplier multiplier = new Multiplier(-999, type, duration, strength);
        multiplierPlugin.getMultiplierAPI().activateMultiplier(multiplier, false);
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
        sender.sendMessage("§cInvalid arguments! §4/" + label + " admin start <type> <duration> <strength>");
        sender.sendMessage(" §8* §7<type> §f- (Word) Multiplier type.");
        sender.sendMessage(" §8* §7<duration> §f- (Number) Duration of the multiplier. (Seconds)");
        sender.sendMessage(" §8* §7<strength> §f- (Number) Strength of the multiplier.");
        sender.sendMessage(" §8* §7§oA duration of -1 will be until the next restart.");
    }
}
