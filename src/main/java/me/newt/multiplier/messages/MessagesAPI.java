package me.newt.multiplier.messages;

import me.newt.multiplier.MultiplierPlugin;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessagesAPI {

    private final FileConfiguration messagesConfig;

    /**
     * Constructor.
     * @param multiplierPlugin Instance of the main class.
     */
    public MessagesAPI(MultiplierPlugin multiplierPlugin) {
        File messagesFile = new File(multiplierPlugin.getDataFolder().getPath(), "language.yml");
        if (!messagesFile.exists()) {
            multiplierPlugin.saveResource("language.yml", false);
        }
        messagesConfig = new YamlConfiguration();
        try {
            messagesConfig.load(messagesFile);
        } catch (IOException | InvalidConfigurationException exception) {
            multiplierPlugin.log(Level.SEVERE, "Failed to create and load the language.yml file.");
            exception.printStackTrace();
        }
    }

    /**
     * Get a formatted message from the language file.
     * @param key Key this message is found under.
     * @return Coloured and formatted message.
     */
    public String get(String key) {
        String message = messagesConfig.getString(key);
        if (message != null) {
            message = translateHexColorCodes(message);
            message = ChatColor.translateAlternateColorCodes('&', message);
            return message;
        }
        return "missing_messages_" + key;
    }

    /**
     * Get a formatted message from the language file and replace placeholder values.
     * @param key          Key this message is found under.
     * @param placeholders Values to replace.
     * @return Coloured and formatted message with placeholders replaced.
     */
    public String get(String key, String... placeholders) {
        String message = get(key);
        for (int i = 0; i < placeholders.length; i++) {
            message = message.replace("{" + i + "}", placeholders[i]);
        }
        return message;
    }

    /**
     * Translate HEX colour codes.
     */
    private String translateHexColorCodes(String message) {
        final Pattern hexPattern = Pattern.compile("<#([A-Fa-f0-9]{6})>");
        Matcher matcher = hexPattern.matcher(message);
        StringBuffer buffer = new StringBuffer(message.length() + 4 * 8);
        while (matcher.find()) {
            String group = matcher.group(1);
            char COLOR_CHAR = ChatColor.COLOR_CHAR;
            matcher.appendReplacement(buffer, COLOR_CHAR + "x"
                    + COLOR_CHAR + group.charAt(0) + COLOR_CHAR + group.charAt(1)
                    + COLOR_CHAR + group.charAt(2) + COLOR_CHAR + group.charAt(3)
                    + COLOR_CHAR + group.charAt(4) + COLOR_CHAR + group.charAt(5)
            );
        }
        return matcher.appendTail(buffer).toString();
    }
}
