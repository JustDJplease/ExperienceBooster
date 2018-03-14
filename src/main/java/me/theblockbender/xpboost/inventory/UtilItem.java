package me.theblockbender.xpboost.inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

/**
 * Easily create itemstacks, without messing your hands.
 *
 * Status: Completed Notice: Makes use of the 'setUnbreakable()' method that was
 * introduced in spigot 1.11.
 *
 * @author NonameSL, modified by TheBlockBender.
 */
public class UtilItem {
    private ItemStack is;

    /**
     * Create a new ItemBuilder from scratch.
     *
     * @param m
     *            The material to create the ItemBuilder with.
     */
    public UtilItem(Material m) {
        this(m, 1);
    }

    /**
     * Create a new ItemBuilder over an existing itemstack.
     *
     * @param is
     *            The itemstack to create the ItemBuilder over.
     */
    public UtilItem(ItemStack is) {
        this.is = is;
    }

    /**
     * Create a new ItemBuilder from scratch.
     *
     * @param m
     *            The material of the item.
     * @param amount
     *            The amount of the item.
     */
    public UtilItem(Material m, int amount) {
        is = new ItemStack(m, amount);
    }

    /**
     * Create a new ItemBuilder from scratch.
     *
     * @param m
     *            The material of the item.
     * @param amount
     *            The amount of the item.
     * @param durability
     *            The durability of the item.
     */
    public UtilItem(Material m, int amount, byte durability) {
        is = new ItemStack(m, amount, durability);
    }

    /**
     * Clone the ItemBuilder into a new one.
     *
     * @return The cloned instance.
     */
    public UtilItem clone() {
        return new UtilItem(is);
    }

    /**
     * Change the durability of the item.
     *
     * @param dur
     *            The durability to set it to.
     */
    public UtilItem setDurability(short dur) {
        is.setDurability(dur);
        return this;
    }

    /**
     * Set the displayname of the item.
     *
     * @param name
     *            The name to change it to.
     */
    public UtilItem setName(String name) {
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(name);
        is.setItemMeta(im);
        return this;
    }

    /**
     * Add an unsafe enchantment.
     *
     * @param ench
     *            The enchantment to add.
     * @param level
     *            The level to put the enchant on.
     */
    public UtilItem addUnsafeEnchantment(Enchantment ench, int level) {
        is.addUnsafeEnchantment(ench, level);
        return this;
    }

    /**
     * Remove a certain enchant from the item.
     *
     * @param ench
     *            The enchantment to remove
     */
    public UtilItem removeEnchantment(Enchantment ench) {
        is.removeEnchantment(ench);
        return this;
    }

    /**
     * Set the skull owner for the item. Works on skulls only.
     *
     * @param owner
     *            The name of the skull's owner.
     */
    @SuppressWarnings("deprecation")
    public UtilItem setSkullOwner(String owner) {
        try {
            SkullMeta im = (SkullMeta) is.getItemMeta();
            im.setOwner(owner);
            is.setItemMeta(im);
        } catch (ClassCastException expected) {
        }
        return this;
    }

    /**
     * Add an enchant to the item.
     *
     * @param ench
     *            The enchant to add
     * @param level
     *            The level
     */
    public UtilItem addEnchant(Enchantment ench, int level) {
        ItemMeta im = is.getItemMeta();
        im.addEnchant(ench, level, true);
        is.setItemMeta(im);
        return this;
    }

    /**
     * Add multiple enchants at once.
     *
     * @param enchantments
     *            The enchants to add.
     */
    public UtilItem addEnchantments(Map<Enchantment, Integer> enchantments) {
        is.addEnchantments(enchantments);
        return this;
    }

    /**
     * Sets infinity durability on the item and clears specific itemflags.
     */
    public UtilItem setInfinityDurability() {
        ItemMeta im = is.getItemMeta();
        im.setUnbreakable(true);
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        im.addItemFlags(ItemFlag.HIDE_DESTROYS);
        im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        im.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        im.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        im.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        is.setItemMeta(im);
        return this;
    }

    /**
     * Re-sets the lore.
     *
     * @param lore
     *            The lore to set it to.
     */
    public UtilItem setLore(String... lore) {
        ItemMeta im = is.getItemMeta();
        im.setLore(Arrays.asList(lore));
        is.setItemMeta(im);
        return this;
    }

    /**
     * Re-sets the lore.
     *
     * @param lore
     *            The lore to set it to.
     */
    public UtilItem setLore(List<String> lore) {
        ItemMeta im = is.getItemMeta();
        im.setLore(lore);
        is.setItemMeta(im);
        return this;
    }

    /**
     * Remove a lore line.
     *
     * @param lore
     *            The lore to remove.
     */
    public UtilItem removeLoreLine(String line) {
        ItemMeta im = is.getItemMeta();
        List<String> lore = new ArrayList<>(im.getLore());
        if (!lore.contains(line))
            return this;
        lore.remove(line);
        im.setLore(lore);
        is.setItemMeta(im);
        return this;
    }

    /**
     * Remove a lore line.
     *
     * @param index
     *            The index of the lore line to remove.
     */
    public UtilItem removeLoreLine(int index) {
        ItemMeta im = is.getItemMeta();
        List<String> lore = new ArrayList<>(im.getLore());
        if (index < 0 || index > lore.size())
            return this;
        lore.remove(index);
        im.setLore(lore);
        is.setItemMeta(im);
        return this;
    }

    /**
     * Add a lore line.
     *
     * @param line
     *            The lore line to add.
     */
    public UtilItem addLoreLine(String line) {
        ItemMeta im = is.getItemMeta();
        List<String> lore = new ArrayList<>();
        if (im.hasLore())
            lore = new ArrayList<>(im.getLore());
        lore.add(line);
        im.setLore(lore);
        is.setItemMeta(im);
        return this;
    }

    /**
     * Add a lore line.
     *
     * @param line
     *            The lore line to add.
     * @param pos
     *            The index of where to put it.
     */
    public UtilItem addLoreLine(String line, int pos) {
        ItemMeta im = is.getItemMeta();
        List<String> lore = new ArrayList<>(im.getLore());
        lore.set(pos, line);
        im.setLore(lore);
        is.setItemMeta(im);
        return this;
    }

    /**
     * Retrieves the itemstack from the ItemBuilder.
     *
     * @return The itemstack created/modified by the ItemBuilder instance.
     */
    public ItemStack toItemStack() {
        return is;
    }


    /**
     * Returns a nicely formatted name of the item.
     *
     */
    public String getItemName() {
        String name = "Unidentified Item";
        if (is.getItemMeta().hasDisplayName()) {
            name = is.getItemMeta().getDisplayName() + " §8(" + is.getAmount() + "x)";
        } else {
            name = format(is.getType().name()) + " §8(" + is.getAmount() + "x)";
        }
        return "§7[§6" + name + "§7]";
    }

    /**
     * Returns a nicely formatted string based on the input.
     *
     */
    private String format(String name) {
        return WordUtils.capitalizeFully(name.replaceAll("_", " "));
    }

    public UtilItem setLoreLine(int index, String newLine) {
        ItemMeta im = is.getItemMeta();
        List<String> lore = im.getLore();
        lore.set(index, newLine);
        im.setLore(lore);
        is.setItemMeta(im);
        return this;
    }

    public UtilItem setGlowing(Boolean guiGlowing) {
        if(guiGlowing) {
            is.addEnchantment(Enchantment.DURABILITY, 10);
        }
        return this;
    }
}
