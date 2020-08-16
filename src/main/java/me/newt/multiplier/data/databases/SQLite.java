package me.newt.multiplier.data.databases;

import me.newt.multiplier.MultiplierPlugin;
import me.newt.multiplier.data.Database;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLite extends Database {

    private final File databaseFile;
    private Connection connection;

    public SQLite(MultiplierPlugin multiplierPlugin) throws IOException {
        databaseFile = new File(multiplierPlugin.getDataFolder().getPath() + "data", "userdata.db");
        if (!databaseFile.exists()) {
            databaseFile.mkdirs();
            databaseFile.createNewFile();
        }
    }

    @Override
    public Connection openConnection() {
        connection = null;
        try {
            String url = "jdbc:sqlite:" + databaseFile.getPath();
            connection = DriverManager.getConnection(url);
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
                    "'id' INT NOT NULL AUTO_INCREMENT," +
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
