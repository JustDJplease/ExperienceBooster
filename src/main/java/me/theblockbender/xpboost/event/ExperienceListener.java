package me.theblockbender.xpboost.event;

import me.theblockbender.xpboost.Main;
import me.theblockbender.xpboost.util.BoosterType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

public class ExperienceListener implements Listener {

    private Main main;

    public ExperienceListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onExperiencePickup(PlayerExpChangeEvent event) {
        if (main.isBoosted(BoosterType.MINECRAFT)) {
            event.setAmount(event.getAmount() * main.getMultiplier(BoosterType.MINECRAFT));
        }
    }
}
