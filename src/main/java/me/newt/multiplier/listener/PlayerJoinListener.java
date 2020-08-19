package me.newt.multiplier.listener;

import me.newt.multiplier.MultiplierPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    MultiplierPlugin multiplierPlugin;

    public PlayerJoinListener(MultiplierPlugin multiplierPlugin) {
        this.multiplierPlugin = multiplierPlugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        multiplierPlugin.getMultiplierAPI().loadMultipliersAsync(event.getPlayer().getUniqueId());
    }
}
