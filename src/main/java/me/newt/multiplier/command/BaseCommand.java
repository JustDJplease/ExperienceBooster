package me.newt.multiplier.command;

import me.newt.multiplier.MultiplierPlugin;
import me.newt.multiplier.command.subcommands.admin.*;
import me.newt.multiplier.command.subcommands.user.MenuCommand;
import me.newt.multiplier.command.subcommands.user.ThankCommand;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class BaseCommand implements CommandExecutor {

    private final MultiplierPlugin multiplierPlugin;
    private final SubCommand menuCMD;
    private final SubCommand thankCMD;
    private final SubCommand giveCMD;
    private final SubCommand listCMD;
    private final SubCommand reloadCMD;
    private final SubCommand removeCMD;
    private final SubCommand startCMD;
    private final SubCommand stopCMD;

    /**
     * Constructor.
     * @param multiplierPlugin Instance of the main class.
     */
    public BaseCommand(MultiplierPlugin multiplierPlugin) {
        this.multiplierPlugin = multiplierPlugin;

        // User subcommands:
        this.menuCMD = new MenuCommand(multiplierPlugin, "multiplier.user.activate");
        this.thankCMD = new ThankCommand(multiplierPlugin, "multiplier.user.thank");

        // Admin subcommands:
        this.giveCMD = new GiveCommand(multiplierPlugin, "multiplier.admin.give");
        this.listCMD = new ListCommand(multiplierPlugin, "multiplier.admin.list");
        this.reloadCMD = new ReloadCommand(multiplierPlugin, "multiplier.admin.reload");
        this.removeCMD = new RemoveCommand(multiplierPlugin, "multiplier.admin.remove");
        this.startCMD = new StartCommand(multiplierPlugin, "multiplier.admin.start");
        this.stopCMD = new StopCommand(multiplierPlugin, "multiplier.admin.stop");

        PluginCommand command = multiplierPlugin.getCommand("multiplier");
        if (command == null) {
            multiplierPlugin.log(Level.SEVERE, "Failed to initialize root command.");
            return;
        }
        command.setExecutor(this);
    }

    /**
     * Command handler.
     * @param sender  Issuer of the command.
     * @param command Executed command.
     * @param label   Alias of the command used.
     * @param args    Arguments used with the command.
     * @return Whether the command was successfully executed (Always true).
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {

            // Menu Command
            if (!(sender instanceof Player)) {
                printHelp(sender, label);
                return true;
            }
            menuCMD.run(sender, label, args);
            return true;

        }
        if (args.length == 1) {

            // Thank Command
            if (args[0].equalsIgnoreCase("thank")) {
                thankCMD.run(sender, label, args);
                return true;
            }

            // Invalid or Help Command
            printHelp(sender, label);
            return true;

        }

        // Admin Command
        if (args[0].equalsIgnoreCase("admin")) {

            // Give Command
            if (args[1].equalsIgnoreCase("give")) {
                giveCMD.run(sender, label, args);
                return true;
            }

            // List Command
            if (args[1].equalsIgnoreCase("list")) {
                listCMD.run(sender, label, args);
                return true;
            }

            // Remove Command
            if (args[1].equalsIgnoreCase("remove")) {
                removeCMD.run(sender, label, args);
                return true;
            }

            // Start Command
            if (args[1].equalsIgnoreCase("start")) {
                startCMD.run(sender, label, args);
                return true;
            }

            // Stop Command
            if (args[1].equalsIgnoreCase("stop")) {
                stopCMD.run(sender, label, args);
                return true;
            }

            // Reload Command
            if (args[1].equalsIgnoreCase("reload")) {
                reloadCMD.run(sender, label, args);
                return true;
            }
        }

        // Invalid Command
        printHelp(sender, label);
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
     * @param sender Issuer of the command.
     * @param label  Alias of the command used.
     */
    private void printHelp(CommandSender sender, String label) {
        sender.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "---------------------------------------------------------------");
        if (sender.hasPermission(menuCMD.getPermission()))
            print(sender, "/" + label, "View and activate your multipliers.");
        if (sender.hasPermission(thankCMD.getPermission()))
            print(sender, "/" + label + " thank", "Show your gratitude for a multiplier.");
        if (sender.hasPermission(listCMD.getPermission()))
            print(sender, "/" + label + " admin list <player>", "List a player's multipliers.");
        if (sender.hasPermission(giveCMD.getPermission()))
            print(sender, "/" + label + " admin give <player> <type> <duration> <strength>", "Give a multiplier.");
        if (sender.hasPermission(removeCMD.getPermission()))
            print(sender, "/" + label + " admin remove <id>", "Remove a multiplier.");
        if (sender.hasPermission(startCMD.getPermission()))
            print(sender, "/" + label + " admin start <type> <duration> <strength>", "Start a multiplier.");
        if (sender.hasPermission(stopCMD.getPermission()))
            print(sender, "/" + label + " admin stop <type>", "Stop active multipliers.");
        if (sender.hasPermission(reloadCMD.getPermission()))
            print(sender, "/" + label + " admin reload", "Reload configuration files.");
        print(sender, "/" + label + " help", "View this list of commands.");
        sender.sendMessage(ChatColor.DARK_GRAY + "" + ChatColor.STRIKETHROUGH + "---------------------------------------------------------------");
    }

    /**
     * Send a TextComponent command help message to a CommandSender.
     * @param sender      Receiver of the message.
     * @param command     Command that should be described.
     * @param description Description of the command.
     */
    private void print(CommandSender sender, String command, String description) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(command + " - " + description);
            return;
        }

        Player player = (Player) sender;
        Text hoverText = new Text(ChatColor.GRAY + "Click to copy this command to your chat.");
        HoverEvent onHover = new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverText);
        ClickEvent onClick = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command);

        BaseComponent[] message = new ComponentBuilder(command)
                .color(ChatColor.GREEN)
                .event(onHover)
                .event(onClick)
                .append(" - ")
                .color(ChatColor.GRAY)
                .append(description)
                .create();

        player.spigot().sendMessage(message);
    }
}
