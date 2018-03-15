package me.theblockbender.xpboost.event;

import com.sucy.skill.api.enums.ExpSource;
import com.sucy.skill.api.event.PlayerExperienceGainEvent;
import com.sucy.skill.api.player.PlayerData;
import me.theblockbender.xpboost.Main;
import me.theblockbender.xpboost.util.BoosterType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SkillAPIListener implements Listener {

    private Main main;
    private List<UUID> skip = new ArrayList<>();

    public SkillAPIListener(Main main) {
        this.main = main;
    }

    // The event.setExp(int amount) method does not actually work.
    @EventHandler
    public void onSkillAPIExperiencePickup(PlayerExperienceGainEvent event) {
        PlayerData player = event.getPlayerData();
        UUID uuid = player.getPlayer().getUniqueId();
        if (skip.contains(uuid)) {
            skip.remove(uuid);
            return;
        }
        if (main.isBoosted(BoosterType.SkillAPI)) {
            event.setCancelled(true);
            double toAdd = (event.getExp() * main.getMultiplier(BoosterType.SkillAPI));
            skip.add(uuid);
            player.giveExp(toAdd, ExpSource.MOB);
        }
    }
}
