package me.theblockbender.xpboost.event;

import me.theblockbender.xpboost.Main;
import me.theblockbender.xpboost.util.BoosterType;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class BottleListener implements Listener {
    private Main main;

    public BottleListener(Main main) {
        this.main = main;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBottleThrow(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (item == null || item.getType() != Material.EXP_BOTTLE) {
            return;
        }
        if (main.isBoosted(BoosterType.Minecraft)
                && main.getConfig().getBoolean("Disable-experience-potions-when-booster-active")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(main.getMessage("event-xpbottle-disabled"));
        }
    }
}
