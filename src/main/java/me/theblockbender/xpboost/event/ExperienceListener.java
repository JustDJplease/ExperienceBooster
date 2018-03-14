package me.theblockbender.xpboost.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

import me.theblockbender.xpboost.Main;
import me.theblockbender.xpboost.util.BoosterType;

public class ExperienceListener implements Listener {

    private Main main;

    public ExperienceListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onExperiencePickup(PlayerExpChangeEvent event) {
        if (main.isBoosted(BoosterType.Minecraft)) {
            event.setAmount(event.getAmount() * main.getMultiplier(BoosterType.Minecraft));
        }
    }
}
