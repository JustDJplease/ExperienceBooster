package me.theblockbender.xpboost.event;

import me.theblockbender.xpboost.Main;
import me.theblockbender.xpboost.util.BoosterType;
import org.bukkit.Material;
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

import java.util.List;
import java.util.UUID;

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
        if (inventory instanceof PlayerInventory) {
            player.sendMessage(main.getMessage("event-wrong-inventory"));
            return;
        }
        player.playSound(player.getLocation(), Sound.ENTITY_ITEMFRAME_ROTATE_ITEM, 0.2f, 1f);
        main.clickCooldown.put(uuid, System.currentTimeMillis() + 500);
        Material bottle = main.getGuiMaterial("gui-booster.material");
        Material chest = main.getGuiMaterial("gui-shop.material");
        Material barrier = main.getGuiMaterial("gui-exit.material");
        if (item.getType() == bottle) {
            if (!item.hasItemMeta())
                return;
            if (!item.getItemMeta().hasLore())
                return;
            List<String> lore = item.getItemMeta().getLore();
            BoosterType type = BoosterType.valueOf(lore.get(lore.size() - 1).replace("§8booster:", "").toUpperCase());
            main.tryActivateBooster(player, type);
            player.closeInventory();
            return;
        }
        if (item.getType() == chest) {
            player.sendMessage(main.getMessage("store-divider"));
            player.sendMessage(" ");
            player.sendMessage(main.getMessage("store-url"));
            player.sendMessage(main.getMessage("store-info"));
            player.sendMessage(" ");
            player.sendMessage(main.getMessage("store-divider"));
            player.closeInventory();
            return;
        }
        if (item.getType() == barrier) {
            player.closeInventory();
        }
    }
}
