package me.theblockbender.xpboost.inventory;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.theblockbender.xpboost.Main;
import me.theblockbender.xpboost.util.BoosterType;

public class BoosterGUI {

    private Main main;
    private Inventory gui;

    public BoosterGUI(Main main) {
        this.main = main;
        gui = Bukkit.createInventory(null, 27, main.getMessage("gui-title"));
        gui.setItem(26,
                new UtilItem(main.getGuiMaterial("gui-exit.material"), main.getGuiAmount("gui-exit.amount"))
                        .setDurability(main.getGuiData("gui-exit.data")).setName(main.getGuiName("gui-exit.name"))
                        .setLore(main.getGuiLore("gui-exit.lore")).setGlowing(main.getGuiGlowing("gui-exit.glow"))
                        .setInfinityDurability().toItemStack());
        ItemStack spacer = new UtilItem(main.getGuiMaterial("gui-spacer.material"),
                main.getGuiAmount("gui-spacer.amount")).setName("§7").setGlowing(main.getGuiGlowing("gui-spacer.glow"))
                .setInfinityDurability().setDurability(main.getGuiData("gui-spacer.data")).toItemStack();
        for (int i = 0; i < 9; i++) {
            gui.setItem(i, spacer);
        }
        for (int i = 18; i < 26; i++) {
            gui.setItem(i, spacer);
        }
        int nextFreeSlot = 11;
        if (main.isBoosterEnabled(BoosterType.Minecraft)) {
            List<String> lore = main.getGuiLore("gui-booster.lore");
            for (int i = 0; i < lore.size(); i++) {
                lore.set(i, lore.get(i)
                        .replace("{duration}",
                                main.utilTime.translateTime(main.getConfig().getInt("Boosters.Minecraft.time") * 1000L))
                        .replace("{multiplier}", "" + main.getConfig().getInt("Boosters.Minecraft.multiplier")));
            }
            lore.add(" ");
            lore.add("§8id: Minecraft");
            gui.setItem(nextFreeSlot, new UtilItem(main.getGuiMaterial("gui-booster.material"), 1)
                    .setName(main.getGuiName("gui-booster.name").replace("{type}",
                            main.getConfig().getString("Boosters.Minecraft.type")))
                    .setLore(lore).setGlowing(main.getGuiGlowing("gui-booster.glowing")).setInfinityDurability()
                    .toItemStack());
            nextFreeSlot++;
        }
        if (main.isBoosterEnabled(BoosterType.SkillAPI)) {
            List<String> lore = main.getGuiLore("gui-booster.lore");
            for (int i = 0; i < lore.size(); i++) {
                lore.set(i, lore.get(i)
                        .replace("{duration}",
                                main.utilTime.translateTime(main.getConfig().getInt("Boosters.SkillAPI.time") * 1000L))
                        .replace("{multiplier}", "" + main.getConfig().getInt("Boosters.SkillAPI.multiplier")));
            }
            lore.add(" ");
            lore.add("§8id: SkillAPI");
            gui.setItem(nextFreeSlot, new UtilItem(main.getGuiMaterial("gui-booster.material"), 1)
                    .setName(main.getGuiName("gui-booster.name").replace("{type}",
                            main.getConfig().getString("Boosters.SkillAPI.type")))
                    .setLore(lore).setGlowing(main.getGuiGlowing("gui-booster.glowing")).setInfinityDurability()
                    .toItemStack());
            nextFreeSlot++;
        }
        if (main.isBoosterEnabled(BoosterType.McMMO)) {
            List<String> lore = main.getGuiLore("gui-booster.lore");
            for (int i = 0; i < lore.size(); i++) {
                lore.set(i, lore.get(i)
                        .replace("{duration}",
                                main.utilTime.translateTime(main.getConfig().getInt("Boosters.McMMO.time") * 1000L))
                        .replace("{multiplier}", "" + main.getConfig().getInt("Boosters.McMMO.multiplier")));
            }
            lore.add(" ");
            lore.add("§8id: McMMO");
            gui.setItem(nextFreeSlot, new UtilItem(main.getGuiMaterial("gui-booster.material"), 1)
                    .setName(main.getGuiName("gui-booster.name").replace("{type}",
                            main.getConfig().getString("Boosters.McMMO.type")))
                    .setLore(lore).setGlowing(main.getGuiGlowing("gui-booster.glowing")).setInfinityDurability()
                    .toItemStack());
            nextFreeSlot++;
        }
        // TODO add item here
        if (main.isBoosterEnabled(BoosterType.Jobs)) {
            List<String> lore = main.getGuiLore("gui-booster.lore");
            for (int i = 0; i < lore.size(); i++) {
                lore.set(i, lore.get(i)
                        .replace("{duration}",
                                main.utilTime.translateTime(main.getConfig().getInt("Boosters.Jobs.time") * 1000L))
                        .replace("{multiplier}", "" + main.getConfig().getInt("Boosters.Jobs.multiplier")));
            }
            lore.add(" ");
            lore.add("§8id: Jobs");
            gui.setItem(nextFreeSlot, new UtilItem(main.getGuiMaterial("gui-booster.material"), 1)
                    .setName(main.getGuiName("gui-booster.name").replace("{type}",
                            main.getConfig().getString("Boosters.Jobs.type")))
                    .setLore(lore).setGlowing(main.getGuiGlowing("gui-booster.glowing")).setInfinityDurability()
                    .toItemStack());
            nextFreeSlot++;
        }

        nextFreeSlot++;
        gui.setItem(nextFreeSlot,
                new UtilItem(main.getGuiMaterial("gui-shop.material"), main.getGuiAmount("gui-shop.amount"))
                        .setDurability(main.getGuiData("gui-shop.data")).setName(main.getGuiName("gui-shop.name"))
                        .setLore(main.getGuiLore("gui-shop.lore")).setGlowing(main.getGuiGlowing("gui-shop.glow"))
                        .setInfinityDurability().toItemStack());
    }

    public void open(Player player) {
        Inventory clone = gui;
        for (int slot = 11; slot < gui.getSize(); slot++) {
            ItemStack item = clone.getItem(slot);
            if (item == null || item.getType() == Material.AIR) {
                break;
            }
            if (item.getType() == main.getGuiMaterial("gui-booster.material")) {
                try {
                    List<String> lore = item.getItemMeta().getLore();
                    BoosterType type = BoosterType.valueOf(lore.get(lore.size() - 1).replace("§8id: ", ""));
                    int i = main.getBoosterAmount(player, type);
                    String s = "";
                    if (i != 1) {
                        s = "s";
                    }
                    for (int l = 0; l < lore.size(); l++) {
                        lore.set(l, lore.get(l).replace("{amount}", i + "").replace("{s}", s));
                    }
                    clone.setItem(slot, new UtilItem(clone.getItem(slot)).setLore(lore).toItemStack());
                } catch (Exception ex) {
                    main.debug(
                            "Invalid booster type on the item in slot " + slot + ", whilst generating the inventory!");
                }
            }

        }
        player.openInventory(clone);
        main.openInventories.add(player.getUniqueId());
        player.playSound(player.getLocation(), Sound.BLOCK_ENDERCHEST_OPEN, 1f, 0.7f);
    }
}
