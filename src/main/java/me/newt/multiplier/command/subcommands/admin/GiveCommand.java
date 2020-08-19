package me.newt.multiplier.command.subcommands.admin;

import me.newt.multiplier.MultiplierType;
import me.newt.multiplier.command.SubCommand;
import me.newt.multiplier.util.UtilArgumentParsers;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class GiveCommand extends SubCommand {

    private final String permission;

    /**
     * Constructor.
     * @param permission Permission required to run this command.
     */
    public GiveCommand(String permission) {
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
        // multiplier admin give <player/uuid> <type> <duration> <strength>

        if (!sender.hasPermission(permission)) {
            sender.sendMessage("§cYou do not have permission. (Lacking: " + permission + ")");
            return;
        }

        if (args.length != 6) {
            printInvalidArgs(sender, label);
            return;
        }

        String unValidatedReceiver = args[2];
        String unValidatedType = args[3];
        String unValidatedDuration = args[4];
        String unValidatedStrength = args[5];

        UUID uuid = UtilArgumentParsers.parseUUID(unValidatedReceiver);
        if (uuid == null) {
            printInvalidArgs(sender, label);
            return;
        }

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

        // TODO GIVE MULTIPLIER
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
        sender.sendMessage("§cInvalid arguments! §4/" + label + " admin give <player> <type> <duration> <strength>");
        sender.sendMessage(" §8* §7<player> §f- (Word) Online player or UUID.");
        sender.sendMessage(" §8* §7<type> §f- (Word) Multiplier type.");
        sender.sendMessage(" §8* §7<duration> §f- (Number) Duration of the multiplier. (Seconds)");
        sender.sendMessage(" §8* §7<strength> §f- (Number) Strength of the multiplier.");
        sender.sendMessage(" §8* §7§oA duration of -1 will be until the next restart.");
    }
}
