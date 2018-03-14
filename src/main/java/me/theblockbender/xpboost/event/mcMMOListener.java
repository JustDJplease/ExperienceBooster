package me.theblockbender.xpboost.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gmail.nossr50.events.experience.McMMOPlayerXpGainEvent;

import me.theblockbender.xpboost.Main;
import me.theblockbender.xpboost.util.BoosterType;

public class mcMMOListener implements Listener {

    private Main main;

    public mcMMOListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onMcMMOExperienceChangeEvent(McMMOPlayerXpGainEvent event) {
        if (main.isBoosted(BoosterType.McMMO)) {
            int multiplier = main.getMultiplier(BoosterType.McMMO);
            event.setRawXpGained(event.getRawXpGained() * multiplier);
            main.debug("Multiplied McMMO Experience for player " + event.getPlayer().getName() + " by " + multiplier
                    + "x.");
        }
    }
}
