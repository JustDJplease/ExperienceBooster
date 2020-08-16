package me.newt.multiplier.data.databases;

import me.newt.multiplier.MultiplierPlugin;
import me.newt.multiplier.data.Database;
import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQL extends Database {

    private Connection connection;
    private final String host;
    private final String port;
    private final String name;
    private final String user;
    private final String pass;


    public MySQL(MultiplierPlugin multiplierPlugin) {
        FileConfiguration config = multiplierPlugin.getConfig();
        host = config.getString("database_host");
        port = config.getString("database_port");
        name = config.getString("database_name");
        user = config.getString("database_username");
        pass = config.getString("database_password");

        assert host != null : "Database credentials (host) in config.yml cannot be null!";
        assert port != null : "Database credentials (port) in config.yml cannot be null!";
        assert name != null : "Database credentials (name) in config.yml cannot be null!";
        assert user != null : "Database credentials (username) in config.yml cannot be null!";
        assert pass != null : "Database credentials (password) in config.yml cannot be null!";
    }

    @Override
    public Connection openConnection() {
        connection = null;
        try {
            String url = "jdbc:mysql://" + host + ":" + port + "/" + name;
            connection = DriverManager.getConnection(url, user, pass);
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return connection;
    }

    @Override
    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void setup() {
        openConnection();
        try {
            Statement s = connection.createStatement();
            String SQLiteCreateTokensTable = "CREATE TABLE IF NOT EXISTS multipliers (" +
                    "'id' INT NOT NULL AUTO_INCREMENT, " +
                    "'uuid' VARCHAR NOT NULL," +
                    "'type' VARCHAR NOT NULL," +
                    "'duration' INT NOT NULL," +
                    "'multiplier' INT NOT NULL," +
                    "PRIMARY KEY ('id'));";
            s.executeUpdate(SQLiteCreateTokensTable);
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection();
    }
}
