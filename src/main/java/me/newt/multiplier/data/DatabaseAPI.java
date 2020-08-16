package me.newt.multiplier.data;

import me.newt.multiplier.Multiplier;
import me.newt.multiplier.MultiplierPlugin;
import me.newt.multiplier.MultiplierType;
import me.newt.multiplier.data.databases.MySQL;
import me.newt.multiplier.data.databases.SQLite;
import org.bukkit.scheduler.BukkitScheduler;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class DatabaseAPI {

    private final MultiplierPlugin multiplierPlugin;
    private final BukkitScheduler scheduler;
    private final Database database;

    /**
     * Constructor.
     * @param multiplierPlugin Instance of the main class.
     * @throws IOException Thrown when a SQLite file cannot be created for whatever reason.
     */
    public DatabaseAPI(MultiplierPlugin multiplierPlugin) throws IOException {
        this.multiplierPlugin = multiplierPlugin;
        this.scheduler = multiplierPlugin.getServer().getScheduler();

        String option = multiplierPlugin.getConfig().getString("database_option");
        assert option != null : "Database option in config.yml cannot be null!";

        if (option.equalsIgnoreCase("MYSQL")) {
            database = new MySQL(multiplierPlugin);
            multiplierPlugin.log(Level.INFO, "Using MySQL to store data.");
        } else if (option.equalsIgnoreCase("SQLITE")) {
            database = new SQLite(multiplierPlugin);
            multiplierPlugin.log(Level.INFO, "Using SQLite to store data.");
        } else {
            database = new SQLite(multiplierPlugin);
            multiplierPlugin.log(Level.WARNING, "Database option in config.yml is configured incorrectly. Falling back to SQLite.");
        }
        scheduler.runTaskAsynchronously(multiplierPlugin, database::setup);
    }

    /**
     * (IS RAN ASYNC) Adds a multiplier to the database that is currently being used.
     * @param uuid       The UUID of the Player who should receive this multiplier.
     * @param multiplier The multiplier that should be added.
     */
    public void addMultiplier(UUID uuid, Multiplier multiplier) {
        scheduler.runTaskAsynchronously(multiplierPlugin, () -> {
            Connection connection = database.openConnection();
            try {
                PreparedStatement statement = connection.prepareStatement("REPLACE INTO multipliers (uuid,type,duration,multiplier) VALUES(?,?,?,?);");
                statement.setString(1, uuid.toString());
                statement.setString(2, multiplier.getType().toString());
                statement.setInt(3, multiplier.getDuration());
                statement.setInt(4, multiplier.getMultiplierStrength());
                statement.executeUpdate();
                statement.close();
            } catch (SQLException exception) {
                multiplierPlugin.log(Level.SEVERE, "Failed to add multiplier to player in the database.");
                exception.printStackTrace();
            }
            database.closeConnection();
        });
    }

    /**
     * (IS RAN ASYNC) Removes a multiplier from the database that is currently being used.
     * @param multiplier The multiplier that should be removed.
     */
    public void removeMultiplier(Multiplier multiplier) {
        scheduler.runTaskAsynchronously(multiplierPlugin, () -> {
            Connection connection = database.openConnection();
            try {
                Statement statement = connection.createStatement();
                String query = "DELETE FROM multipliers WHERE id = " + multiplier.getId() + ";";
                statement.executeUpdate(query);
                statement.close();
            } catch (SQLException exception) {
                multiplierPlugin.log(Level.SEVERE, "Failed to remove multiplier from player in the database.");
                exception.printStackTrace();
            }
            database.closeConnection();
        });
    }

    /**
     * (SHOULD BE RAN ASYNC) Obtain a list of multipliers owned by a Player.
     * @param uuid The UUID of the Player.
     */
    public List<Multiplier> getMultipliers(UUID uuid) {
        Connection connection = database.openConnection();
        List<Multiplier> multipliers = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT id, type, duration, multiplier FROM multipliers WHERE uuid = '" + uuid + "';";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {

                int id = resultSet.getInt("id");
                int duration = resultSet.getInt("duration");
                int multiplierStrength = resultSet.getInt("multiplier");
                MultiplierType type = MultiplierType.valueOf(resultSet.getString("type").toUpperCase());

                Multiplier multiplier = new Multiplier(id, type, duration, multiplierStrength);
                multipliers.add(multiplier);
            }
        } catch (SQLException exception) {
            multiplierPlugin.log(Level.SEVERE, "Failed to get multipliers from player in the database.");
            exception.printStackTrace();
        }
        database.closeConnection();
        return multipliers;
    }
}
