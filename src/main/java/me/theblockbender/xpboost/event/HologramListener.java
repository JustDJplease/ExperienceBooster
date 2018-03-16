package me.theblockbender.xpboost.event;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import me.theblockbender.xpboost.Main;
import me.theblockbender.xpboost.util.BoosterType;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class HologramListener implements Listener {

    private Main main;

    public HologramListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerKillEntity(EntityDeathEvent event) {
        if (!main.spawnHolos)
            return;
        if (!main.getConfig().getBoolean("MINECRAFT.hologram-when-not-boosted") && !main.getConfig().getBoolean("MINECRAFT.hologram-when-boosted") && !main.getConfig().getBoolean("SKILLAPI.hologram-when-not-boosted") && !main.getConfig().getBoolean("SKILLAPI.hologram-when-boosted")) {
            return;
        }
        Entity e = event.getEntity();
        Location loc = e.getLocation().clone().add(0, 1.0, 0);
        Hologram hologram = HologramsAPI.createHologram(main, loc);
        boolean boosted = false;
        if (main.isBoosted(BoosterType.MINECRAFT) && main.getConfig().getBoolean("MINECRAFT.hologram-when-boosted")) {
            if (event.getDroppedExp() > 0) {
                int multi = main.getMultiplier(BoosterType.MINECRAFT);
                int exp = event.getDroppedExp();
                int expmulti = multi * exp;
                for (String s : main.getConfig().getStringList("MINECRAFT.hologram-text-when-boosted")) {
                    hologram.appendTextLine(ChatColor.translateAlternateColorCodes('&', s.replace("{amount}", exp + "").replace("{amount-multiplied}", expmulti + "").replace("{multiplier}", multi + "").replace("{multiplier-name}", main.getMultiplierName(BoosterType.MINECRAFT))));
                }
            }
            boosted = true;
        }
        if (main.isBoosted(BoosterType.SKILLAPI) && main.getConfig().getBoolean("SKILLAPI.hologram-when-boosted")) {
            if (event.getDroppedExp() > 0) {
                int multi = main.getMultiplier(BoosterType.SKILLAPI);
                int exp = event.getDroppedExp();
                int expmulti = multi * exp;
                for (String s : main.getConfig().getStringList("SKILLAPI.hologram-text-when-boosted")) {
                    hologram.appendTextLine(ChatColor.translateAlternateColorCodes('&', s.replace("{amount}", exp + "").replace("{amount-multiplied}", expmulti + "").replace("{multiplier}", multi + "").replace("{multiplier-name}", main.getMultiplierName(BoosterType.SKILLAPI))));
                }
            }
            boosted = true;
        }
        if (boosted) {
            hologram.teleport(hologram.getLocation().add(0, 0.3 * hologram.size(), 0));
            return;
        }
        if (main.getConfig().getBoolean("MINECRAFT.hologram-when-not-boosted")) {
            if (event.getDroppedExp() > 0) {
                int exp = event.getDroppedExp();
                for (String s : main.getConfig().getStringList("MINECRAFT.hologram-text-when-not-boosted")) {
                    hologram.appendTextLine(ChatColor.translateAlternateColorCodes('&', s.replace("{amount}", exp + "")));
                }
            }
        }
        if (main.getConfig().getBoolean("SKILLAPI.hologram-when-not-boosted")) {
            if (event.getDroppedExp() > 0) {
                int exp = event.getDroppedExp();
                for (String s : main.getConfig().getStringList("SKILLAPI.hologram-text-when-not-boosted")) {
                    hologram.appendTextLine(ChatColor.translateAlternateColorCodes('&', s.replace("{amount}", exp + "")));
                }
            }
        }
        hologram.teleport(hologram.getLocation().add(0, 0.3 * hologram.size(), 0));
    }
}
