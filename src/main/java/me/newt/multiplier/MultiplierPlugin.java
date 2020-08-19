package me.newt.multiplier;

import me.newt.multiplier.command.BaseCommand;
import me.newt.multiplier.data.DatabaseAPI;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.logging.Level;

public class MultiplierPlugin extends JavaPlugin {

    public MultiplierAPI multiplierAPI;
    public DatabaseAPI databaseAPI;

    public boolean dependencyJobsLoaded;
    public boolean dependencyMcMMOLoaded;
    public boolean dependencyPAPILoaded;

    /*
     * TODO Files in the plugin directory:
     * /plugins/ExperienceMultiplier/config.yml
     * /plugins/ExperienceMultiplier/language.yml
     * /plugins/ExperienceMultiplier/data/userdata.db
     */

    /**
     * Enabling the plugin.
     */
    @Override
    public void onEnable() {
        try {
            databaseAPI = new DatabaseAPI(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        getCommand("multiplier").setExecutor(new BaseCommand(this));
    }

    /**
     * Disabling the plugin.
     */
    @Override
    public void onDisable() {

    }

    /**
     * Log a message in the console.
     * @param level   The severity-level of the message.
     * @param message The message that should be logged.
     */
    public void log(Level level, String message) {
        getLogger().log(level, message);
    }
}
