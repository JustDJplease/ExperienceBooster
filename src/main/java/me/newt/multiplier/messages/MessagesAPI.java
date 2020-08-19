package me.newt.multiplier.messages;

import me.newt.multiplier.MultiplierPlugin;

public class MessagesAPI {

    private final MultiplierPlugin multiplierPlugin;

    /**
     * Constructor.
     * @param multiplierPlugin Instance of the main class.
     */
    public MessagesAPI(MultiplierPlugin multiplierPlugin) {
        this.multiplierPlugin = multiplierPlugin;
    }

    public String get(String key) {
        String message = "TODO get the message here.";
        return message;
    }

    public String get(String key, String... placeholders) {
        String message = get(key);
        for (int i = 0; i < placeholders.length; i++) {
            message = message.replace("{" + i + "}", placeholders[i]);
        }
        return message;
    }
}
