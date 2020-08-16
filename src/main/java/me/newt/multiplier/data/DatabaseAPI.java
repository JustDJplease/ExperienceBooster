package me.newt.multiplier.data;

import me.newt.multiplier.Multiplier;
import me.newt.multiplier.MultiplierPlugin;
import me.newt.multiplier.data.databases.MySQL;
import me.newt.multiplier.data.databases.SQLite;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class DatabaseAPI {

    private final Database database;

    /**
     * Constructor.
     * @param multiplierPlugin Instance of the main class.
     * @throws IOException Thrown when a SQLite file cannot be created for whatever reason.
     */
    public DatabaseAPI(MultiplierPlugin multiplierPlugin) throws IOException {
        String option = multiplierPlugin.getConfig().getString("database_option");
        assert option != null : "Database option in config.yml cannot be null!";

        if (option.equalsIgnoreCase("MYSQL")) {
            database = new MySQL(multiplierPlugin);
        } else if (option.equalsIgnoreCase("SQLITE")) {
            database = new SQLite(multiplierPlugin);
        } else {
            database = new SQLite(multiplierPlugin);
            multiplierPlugin.log(Level.WARNING, "Database option in config.yml is configured incorrectly. Falling back to SQLite.");
        }
    }

    /**
     * Add a multiplier to the database that is currently being used.
     * @param multiplier The multiplier that should be added.
     */
    public void addMultiplier(Multiplier multiplier) {
        Connection connection = database.openConnection();

        database.closeConnection();
    }

    /**
     * Remove a multiplier from the database that is currently being used.
     * @param multiplier The multiplier that should be removed.
     */
    public void removeMultiplier(Multiplier multiplier) {
        Connection connection = database.openConnection();
        // TODO ..
        database.closeConnection();
    }

    /**
     * Obtain a list of multipliers owned by a Player.
     * @param uuid The UUID of the Player.
     */
    public List<Multiplier> getMultipliers(UUID uuid) {
        Connection connection = database.openConnection();
        // TODO ..
        database.closeConnection();
        return null;
    }
}
