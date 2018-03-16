package me.theblockbender.xpboost.event;

import com.gmail.nossr50.events.experience.McMMOPlayerXpGainEvent;
import me.theblockbender.xpboost.Main;
import me.theblockbender.xpboost.util.BoosterType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class mcMMOListener implements Listener {

    private Main main;

    public mcMMOListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onMcMMOExperienceChangeEvent(McMMOPlayerXpGainEvent event) {
        if (main.isBoosted(BoosterType.MCMMO)) {
            int multiplier = main.getMultiplier(BoosterType.MCMMO);
            event.setRawXpGained(event.getRawXpGained() * multiplier);
            main.debug("Multiplied MCMMO Experience for player " + event.getPlayer().getName() + " by " + multiplier
                    + "x.");
        }
    }
}
