package me.theblockbender.xpboost.event;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import me.theblockbender.xpboost.Main;
import me.theblockbender.xpboost.util.BoosterType;
import net.md_5.bungee.api.ChatColor;

public class HologramListener implements Listener {

    private Main main;

    public HologramListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerKillEntity(EntityDeathEvent event) {
        Entity e = event.getEntity();
        boolean boosted = false;
        if (main.isBoosted(BoosterType.Minecraft) && main.getConfig().getBoolean("Indicator.spawn-minecraft")) {
            if (event.getDroppedExp() > 0) {
                Location loc = e.getLocation().clone().add(0, 1.3, 0).add(0, -10, 0);
                ArmorStand stand = (ArmorStand) e.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
                stand.setVisible(false);
                stand.setGravity(false);
                stand.setSmall(true);
                stand.setCustomNameVisible(true);
                int multi = main.getMultiplier(BoosterType.Minecraft);
                int exp = event.getDroppedExp();
                stand.setCustomName(ChatColor.translateAlternateColorCodes('&',
                        main.getConfig().getString("Indicator.name-minecraft").replace("{multiplier}", multi + "")
                                .replace("{amount}", exp + "")
                                .replace("{multiplier-word}", main.getMultiplierName(BoosterType.Minecraft))
                                .replace("{amount-multiplied}", "" + multi * exp)));
                stand.setMarker(true);
                main.xpNotifiers.put(stand.getUniqueId(), System.currentTimeMillis() + 2000);
                main.moveThese.put(stand, loc.clone().add(0, 10, 0));
            }
            boosted = true;
        }
        if (main.isBoosted(BoosterType.SkillAPI) && main.getConfig().getBoolean("Indicator.spawn-skillapi")) {
            if (event.getDroppedExp() > 0) {
                Location loc = e.getLocation().clone().add(0, 1.3, 0).add(0, -10, 0);
                if (boosted) {
                    loc.add(0, 0.3, 0);
                }
                ArmorStand stand = (ArmorStand) e.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
                stand.setVisible(false);
                stand.setGravity(false);
                stand.setSmall(true);
                stand.setCustomNameVisible(true);
                int multi = main.getMultiplier(BoosterType.SkillAPI);
                int exp = event.getDroppedExp();
                stand.setCustomName(ChatColor.translateAlternateColorCodes('&',
                        main.getConfig().getString("Indicator.name-skillapi").replace("{multiplier}", multi + "")
                                .replace("{amount}", exp + "")
                                .replace("{multiplier-word}", main.getMultiplierName(BoosterType.SkillAPI))
                                .replace("{amount-multiplied}", "" + multi * exp)));
                stand.setMarker(true);
                main.xpNotifiers.put(stand.getUniqueId(), System.currentTimeMillis() + 2000);
                main.moveThese.put(stand, loc.clone().add(0, 10, 0));
            }
            boosted = true;
        }
        if (boosted) {
            return;
        }
        if (main.getConfig().getBoolean("Indicator.spawn-when-no-booster")) {
            if (event.getDroppedExp() > 0) {
                Location loc = e.getLocation().clone().add(0, 1.3, 0).add(0, -10, 0);
                ArmorStand stand = (ArmorStand) e.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
                stand.setVisible(false);
                stand.setGravity(false);
                stand.setSmall(true);
                stand.setCustomNameVisible(true);
                stand.setCustomName(ChatColor.translateAlternateColorCodes('&', main.getConfig()
                        .getString("Indicator.name-when-no-booster").replace("{amount}", event.getDroppedExp() + "")));
                stand.setMarker(true);
                main.xpNotifiers.put(stand.getUniqueId(), System.currentTimeMillis() + 2000);
                main.moveThese.put(stand, loc.clone().add(0, 10, 0));
            }
        }
    }
}
