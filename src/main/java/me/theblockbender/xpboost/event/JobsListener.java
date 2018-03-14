package me.theblockbender.xpboost.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gamingmesh.jobs.api.JobsExpGainEvent;

import me.theblockbender.xpboost.Main;
import me.theblockbender.xpboost.util.BoosterType;

public class JobsListener implements Listener {

    private Main main;

    public JobsListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onJobExpChange(JobsExpGainEvent event) {
        if (main.isBoosted(BoosterType.Jobs)) {
            int multiplier = main.getMultiplier(BoosterType.Jobs);
            event.setExp(event.getExp() * multiplier);
            main.debug("Multiplied Jobs Experience for player " + event.getPlayer().getName() + " by " + multiplier
                    + "x.");
        }
    }
}
