package me.theblockbender.xpboost.event;

import java.util.UUID;

import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import me.theblockbender.xpboost.Main;
import me.theblockbender.xpboost.util.BoosterType;
import net.md_5.bungee.api.ChatColor;

public class InventoryListener implements Listener {

    private Main main;

    public InventoryListener(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        main.openInventories.remove(uuid);
        main.clickCooldown.remove(uuid);
    }

    @EventHandler
    public void onInventoryItemClick(InventoryClickEvent event) {
        HumanEntity human = event.getWhoClicked();
        if (!(human instanceof Player)) {
            human.sendMessage(main.getMessage("command-from-console"));
            return;
        }
        Player player = (Player) human;
        UUID uuid = player.getUniqueId();
        if (!main.openInventories.contains(uuid)) {
            return;
        }
        event.setCancelled(true);
        if (main.clickCooldown.containsKey(uuid)) {
            if (main.clickCooldown.get(uuid) > System.currentTimeMillis()) {
                return;
            }
        }
        Inventory inventory = event.getInventory();
        int slot = event.getSlot();
        if (slot == -999) {
            return;
        }
        ItemStack item = inventory.getItem(slot);
        if (item == null) {
            return;
        }
        if(inventory instanceof PlayerInventory) {
            player.sendMessage(main.getMessage("event-wrong-inventory"));
            return;
        }
        player.playSound(player.getLocation(), Sound.ENTITY_ITEMFRAME_ROTATE_ITEM, 0.2f, 1f);
        main.clickCooldown.put(uuid, System.currentTimeMillis() + 500);
        switch (item.getType()) {
            case EXP_BOTTLE:
                if (ChatColor.stripColor(item.getItemMeta().getLore().get(4)).contains(" 0 ")) {
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_BREAK, 0.3f, 1f);
                    player.sendMessage(main.getMessage("command-no-booster"));
                    return;
                }
                BoosterType booster;
                String name = ChatColor.stripColor(item.getItemMeta().getDisplayName()).replace("Activate a ", "").replace(" Experience Booster", "");
                booster = main.getBoosterValue(name);
                if(booster == null) {
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_BREAK, 0.3f, 1f);
                    player.sendMessage(main.getMessage("event-bugreport"));
                    return;
                }
                main.tryActivateBooster(player, booster);
                player.closeInventory();
                return;
            case INK_SACK:
                player.sendMessage(main.getMessage("store-divider"));
                player.sendMessage(" ");
                player.sendMessage(main.getMessage("store-url"));
                player.sendMessage(main.getMessage("store-info"));
                player.sendMessage(" ");
                player.sendMessage(main.getMessage("store-divider"));
                player.closeInventory();
                return;
            case BARRIER:
                player.closeInventory();
                return;
            default:
        }
    }
}
