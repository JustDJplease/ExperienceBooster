package me.theblockbender.xpboost;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import me.theblockbender.xpboost.command.BoosterCommand;
import me.theblockbender.xpboost.event.*;
import me.theblockbender.xpboost.inventory.BoosterGUI;
import me.theblockbender.xpboost.util.Booster;
import me.theblockbender.xpboost.util.BoosterType;
import me.theblockbender.xpboost.util.UtilTime;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Main extends JavaPlugin implements Listener {

    public BoosterGUI boostergui;
    public UtilTime utilTime = new UtilTime(this);
    public List<UUID> openInventories = new ArrayList<>();
    public Map<UUID, Long> clickCooldown = new HashMap<>();
    public boolean spawnHolos = true;
    private File storagef;
    private FileConfiguration storage;
    private FileConfiguration messages;
    private BossBar bar_minecraft = null;
    private BossBar bar_skillapi = null;
    private BossBar bar_mcmmo = null;
    // Future add bar here.
    private BossBar bar_jobs = null;
    private List<Booster> activeBoosters = new ArrayList<>();

    public void onEnable() {
        saveDefaultConfig();
        createFiles();
        PluginManager pm = Bukkit.getPluginManager();
        if (pm.getPlugin("HolographicDisplays") == null) {
            spawnHolos = false;
            getLogger().warning("----------------------------------------------------------------------------");
            getLogger().warning("HolographicDisplays dependency is missing!");
            getLogger().warning("Please install the latest version of HolographicDisplays. It can be gotten");
            getLogger().warning("from: https://dev.bukkit.org/projects/holographic-displays");
            getLogger().warning(" ");
            getLogger().warning("[IMPORTANT] No holograms will be shown without this plugin!");
            getLogger().warning("----------------------------------------------------------------------------");
        }
        if (getConfig().getDouble("version") < 0.7) {
            getLogger().severe("----------------------------------------------------------------------------");
            getLogger().severe("Your configuration file for this plugin is to old!");
            getLogger().severe("Delete the current plugins/ExperienceBooster folder to enable this plugin!");
            getLogger().severe("----------------------------------------------------------------------------");
            pm.disablePlugin(this);
            return;
        }
        pm.registerEvents(new InventoryListener(this), this);
        pm.registerEvents(new HologramListener(this), this);
        pm.registerEvents(new BottleListener(this), this);
        if (isBoosterEnabled(BoosterType.MINECRAFT)) {
            pm.registerEvents(new ExperienceListener(this), this);
            debug("Activated MC Listener");
        }
        if (isBoosterEnabled(BoosterType.SKILLAPI)) {
            Plugin skill = pm.getPlugin("SkillAPI");
            if (skill != null) {
                pm.registerEvents(new SkillAPIListener(this), this);
                debug("Activated SkillAPI Listener");
            } else {
                getLogger().warning(
                        "You cannot enable 'SkillAPI' multiplication if you do not have 'SkillAPI' installed!");
            }
        }
        if (isBoosterEnabled(BoosterType.MCMMO)) {
            Plugin skill = pm.getPlugin("mcMMO");
            if (skill != null) {
                pm.registerEvents(new mcMMOListener(this), this);
                debug("Activated mcMMO Listener");
            } else {
                getLogger().warning("You cannot enable 'mcMMO' multiplication if you do not have 'mcMMO' installed!");
            }
        }
        // Future Add enabled check here
        if (isBoosterEnabled(BoosterType.JOBS)) {
            Plugin skill = pm.getPlugin("Jobs");
            if (skill != null) {
                pm.registerEvents(new JobsListener(this), this);
                debug("Activated Jobs Listener");
            } else {
                getLogger().warning("You cannot enable 'Jobs' multiplication if you do not have 'Jobs' installed!");
            }
        }
        getCommand("xpboost").setExecutor(new BoosterCommand(this));
        boostergui = new BoosterGUI(this);

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            // Clear inventory cooldown list, incase someone disapears.
            clickCooldown.entrySet().removeIf(pair -> System.currentTimeMillis() > pair.getValue());
        }, 60 * 20L, 60 * 20L);

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            FileConfiguration config = getConfig();
            // MINECRAFT
            if (isBoosted(BoosterType.MINECRAFT)) {
                if (bar_minecraft == null) {
                    try {
                        bar_minecraft = Bukkit.createBossBar(ChatColor.translateAlternateColorCodes('&',
                                config.getString("Boosters.MINECRAFT.bossbar-message")
                                        .replace("{player}", getWhoIsBoosting(BoosterType.MINECRAFT))
                                        .replace("{time}", getTimeLeft(BoosterType.MINECRAFT))
                                        .replace("{current-multiplier}", getMultiplierName(BoosterType.MINECRAFT))),
                                BarColor.valueOf(config.getString("Boosters.MINECRAFT.bossbar-color").toUpperCase()),
                                BarStyle.SOLID);
                    }catch(NullPointerException ex){
                        ex.printStackTrace();
                    }
                }
                try {
                    bar_minecraft.setTitle(ChatColor.translateAlternateColorCodes('&',
                            config.getString("Boosters.MINECRAFT.bossbar-message")
                                    .replace("{player}", getWhoIsBoosting(BoosterType.MINECRAFT))
                                    .replace("{time}", getTimeLeft(BoosterType.MINECRAFT))
                                    .replace("{current-multiplier}", getMultiplierName(BoosterType.MINECRAFT))));
                    for (Player online : Bukkit.getOnlinePlayers()) {
                        if (!bar_minecraft.getPlayers().contains(online)) {
                            bar_minecraft.addPlayer(online);
                        }
                    }
                } catch (NullPointerException ex) {
                    debug("Bar task created a NPE (MINECRAFT). It was silenced.");
                }
            }
            // SKILLAPI
            if (isBoosted(BoosterType.SKILLAPI)) {
                if (bar_skillapi == null) {
                    bar_skillapi = Bukkit.createBossBar(ChatColor.translateAlternateColorCodes('&',
                            config.getString("Boosters.SKILLAPI.bossbar-message")
                                    .replace("{player}", getWhoIsBoosting(BoosterType.SKILLAPI))
                                    .replace("{time}", getTimeLeft(BoosterType.SKILLAPI))
                                    .replace("{current-multiplier}", getMultiplierName(BoosterType.SKILLAPI))),
                            BarColor.valueOf(config.getString("Boosters.SKILLAPI.bossbar-color").toUpperCase()),
                            BarStyle.SOLID);
                }
                try {
                    bar_skillapi.setTitle(ChatColor.translateAlternateColorCodes('&',
                            config.getString("Boosters.SKILLAPI.bossbar-message")
                                    .replace("{player}", getWhoIsBoosting(BoosterType.SKILLAPI))
                                    .replace("{time}", getTimeLeft(BoosterType.SKILLAPI))
                                    .replace("{current-multiplier}", getMultiplierName(BoosterType.SKILLAPI))));
                    for (Player online : Bukkit.getOnlinePlayers()) {
                        if (!bar_skillapi.getPlayers().contains(online)) {
                            bar_skillapi.addPlayer(online);
                        }
                    }
                } catch (NullPointerException ex) {
                    debug("Bar task created a NPE (SKILLAPI). It was silenced.");
                }
            }
            // MCMMO
            if (isBoosted(BoosterType.MCMMO)) {
                if (bar_mcmmo == null) {
                    bar_mcmmo = Bukkit.createBossBar(
                            ChatColor.translateAlternateColorCodes('&',
                                    config.getString("Boosters.MCMMO.bossbar-message")
                                            .replace("{player}", getWhoIsBoosting(BoosterType.MCMMO))
                                            .replace("{time}", getTimeLeft(BoosterType.MCMMO))
                                            .replace("{current-multiplier}", getMultiplierName(BoosterType.MCMMO))),
                            BarColor.valueOf(config.getString("Boosters.MCMMO.bossbar-color").toUpperCase()),
                            BarStyle.SOLID);
                }
                try {
                    bar_mcmmo.setTitle(ChatColor.translateAlternateColorCodes('&',
                            config.getString("Boosters.MCMMO.bossbar-message")
                                    .replace("{player}", getWhoIsBoosting(BoosterType.MCMMO))
                                    .replace("{time}", getTimeLeft(BoosterType.MCMMO))
                                    .replace("{current-multiplier}", getMultiplierName(BoosterType.MCMMO))));
                    for (Player online : Bukkit.getOnlinePlayers()) {
                        if (!bar_mcmmo.getPlayers().contains(online)) {
                            bar_mcmmo.addPlayer(online);
                        }
                    }
                } catch (NullPointerException ex) {
                    debug("Bar task created a NPE (MCMMO). It was silenced.");
                }
            }
            // Future add bar task here.
            // JOBS
            if (isBoosted(BoosterType.JOBS)) {
                if (bar_jobs == null) {
                    bar_jobs = Bukkit.createBossBar(
                            ChatColor.translateAlternateColorCodes('&',
                                    config.getString("Boosters.JOBS.bossbar-message")
                                            .replace("{player}", getWhoIsBoosting(BoosterType.JOBS))
                                            .replace("{time}", getTimeLeft(BoosterType.JOBS))
                                            .replace("{current-multiplier}", getMultiplierName(BoosterType.JOBS))),
                            BarColor.valueOf(config.getString("Boosters.JOBS.bossbar-color").toUpperCase()),
                            BarStyle.SOLID);
                }
                try {
                    bar_jobs.setTitle(ChatColor.translateAlternateColorCodes('&',
                            config.getString("Boosters.JOBS.bossbar-message")
                                    .replace("{player}", getWhoIsBoosting(BoosterType.JOBS))
                                    .replace("{time}", getTimeLeft(BoosterType.JOBS))
                                    .replace("{current-multiplier}", getMultiplierName(BoosterType.JOBS))));
                    for (Player online : Bukkit.getOnlinePlayers()) {
                        if (!bar_jobs.getPlayers().contains(online)) {
                            bar_jobs.addPlayer(online);
                        }
                    }
                } catch (NullPointerException ex) {
                    debug("Bar task created a NPE (JOBS). It was silenced.");
                }
            }
            // Despawn hologram task
            if (spawnHolos) {
                for (Hologram holo : HologramsAPI.getHolograms(this)) {
                    if (holo.getCreationTimestamp() + 1750 < System.currentTimeMillis()) {
                        holo.delete();
                    }
                }
            }
        }, 20L, 20L);
    }

    public void onDisable() {
        boostergui = null;
        // Closing all open inventories
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (openInventories.contains(player.getUniqueId())) {
                player.closeInventory();
                player.sendMessage(getMessage("event-plugin-disabled"));
                player.sendMessage(getMessage("event-force-close-inventory"));
            }
        }
        openInventories.clear();
        clickCooldown.clear();
        // Cleaning all boss bars
        if (bar_minecraft != null) {
            bar_minecraft.removeAll();
            bar_minecraft = null;
        }
        if (bar_skillapi != null) {
            bar_skillapi.removeAll();
            bar_skillapi = null;
        }
        if (bar_mcmmo != null) {
            bar_mcmmo.removeAll();
            bar_mcmmo = null;
        }
        // Future add cleaning task here
        if (bar_jobs != null) {
            bar_jobs.removeAll();
            bar_jobs = null;
        }
        // Despawning the active holograms
        if (spawnHolos) {
            for (Hologram holo : HologramsAPI.getHolograms(this)) {
                holo.delete();
            }
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void createFiles() {
        storagef = new File(getDataFolder(), "playerdata.yml");
        File messagesf = new File(getDataFolder(), "language.yml");
        if (!storagef.exists()) {
            storagef.getParentFile().mkdirs();
            saveResource("playerdata.yml", false);
        }
        if (!messagesf.exists()) {
            messagesf.getParentFile().mkdirs();
            saveResource("language.yml", false);
        }
        storage = new YamlConfiguration();
        messages = new YamlConfiguration();
        try {
            storage.load(storagef);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        try {
            messages.load(messagesf);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void endActiveBooster(Booster booster) {
        Bukkit.broadcastMessage(getMessage("booster-ended").replace("{player}", booster.getPlayerName())
                .replace("{type}", getConfig().getString("Boosters." + booster.getType().name() + ".type")));
        activeBoosters.remove(booster);
        if (isBoosted(booster.getType())) {
            debug("A booster finished. Since there was however still a booster of this type active, the bar will remain.");
        } else {
            debug("A booster finished. The bar was cleared.");
            switch (booster.getType()) {
                case MCMMO:
                    bar_mcmmo.removeAll();
                    bar_mcmmo = null;
                    return;
                case MINECRAFT:
                    bar_minecraft.removeAll();
                    bar_minecraft = null;
                    return;
                case SKILLAPI:
                    bar_skillapi.removeAll();
                    bar_skillapi = null;
                    return;
                // Future Add type here
                case JOBS:
                    bar_jobs.removeAll();
                    bar_jobs = null;
            }
        }
    }

    public void addBooster(Player giveTo, int a, CommandSender cmd, BoosterType type) {
        String boostername = getConfig().getString("Boosters." + type.name() + ".type");
        String s = "";
        if (a != 1) {
            s = "s";
        }
        String uuid = giveTo.getUniqueId().toString();
        if (storage.contains(uuid + "." + type.name())) {
            int amount = storage.getInt(uuid + "." + type.name()) + a;
            String sa = "";
            if (amount != 1) {
                sa = "s";
            }
            storage.set(uuid + "." + type.name(), amount);
            cmd.sendMessage(getMessage("command-give").replace("{player}", giveTo.getName())
                    .replace("{type}", boostername).replace("{amount}", a + "").replace("{s}", s));
            cmd.sendMessage(getMessage("command-current-amount").replace("{amount}", amount + ""));
            giveTo.sendMessage(getMessage("booster-receive").replace("{amount}", a + "").replace("{type}", boostername)
                    .replace("{s}", sa));
            try {
                storage.save(storagef);
            } catch (IOException e) {
                cmd.sendMessage(getMessage("command-error-save"));
                debug("IOException whilst saving player data.");
                e.printStackTrace();
            }
        } else {
            storage.set(uuid + "." + type.name(), a);
            cmd.sendMessage(getMessage("command-give").replace("{player}", giveTo.getName())
                    .replace("{type}", boostername).replace("{amount}", a + "").replace("{s}", s));
            cmd.sendMessage(getMessage("command-current-amount").replace("{amount}", a + ""));
            giveTo.sendMessage(getMessage("booster-receive").replace("{amount}", a + "").replace("{type}", boostername)
                    .replace("{s}", s));
            try {
                storage.save(storagef);
            } catch (IOException e) {
                cmd.sendMessage(getMessage("command-error-save"));
                debug("IOException whilst saving player data.");
                e.printStackTrace();
            }
        }
    }

    public void takeBooster(Player takeFrom, int a, CommandSender cmd, BoosterType type) {
        String boostername = getConfig().getString("Boosters." + type.name() + ".type");
        String s = "";
        if (a != 1) {
            s = "s";
        }
        String uuid = takeFrom.getUniqueId().toString();
        if (storage.contains(uuid + "." + type.name())) {
            int amount = storage.getInt(uuid + "." + type.name());
            int subtracted = amount - a;
            if (subtracted < 1) {
                storage.set(uuid + "." + type.name(), null);
                cmd.sendMessage(getMessage("command-reset").replace("{player}", takeFrom.getName()).replace("{type}",
                        boostername));
                cmd.sendMessage(getMessage("command-current-amount").replace("{amount}", "0"));
            } else {
                storage.set(uuid + "." + type.name(), subtracted);
                cmd.sendMessage(getMessage("command-take").replace("{amount}", a + "").replace("{type}", boostername)
                        .replace("player", takeFrom.getName()).replace("{s}", s));
                cmd.sendMessage(getMessage("command-current-amount").replace("{amount}", subtracted + ""));
            }
            try {
                storage.save(storagef);
            } catch (IOException e) {
                cmd.sendMessage(getMessage("command-error-save"));
                debug("IOException whilst saving player data.");
                e.printStackTrace();
            }
        } else {
            cmd.sendMessage(getMessage("command-error-no-boosters").replace("{player}", takeFrom.getName())
                    .replace("{type}", boostername));
        }
    }

    public void resetBooster(Player reset, CommandSender cmd, BoosterType type) {
        String boostername = getConfig().getString("Boosters." + type.name() + ".type");
        String uuid = reset.getUniqueId().toString();
        if (storage.contains(uuid + "." + type.name())) {
            storage.set(uuid + "." + type.name(), null);
            cmd.sendMessage(
                    getMessage("command-reset").replace("{player}", reset.getName()).replace("{type}", boostername));
            try {
                storage.save(storagef);
            } catch (IOException e) {
                cmd.sendMessage(getMessage("command-error-save"));
                debug("IOException whilst saving player data.");
                e.printStackTrace();
            }
        } else {
            cmd.sendMessage(getMessage("command-error-no-boosters").replace("{player}", reset.getName())
                    .replace("{type}", boostername));
        }
    }

    public void cmdActivate(Player sender, BoosterType type) {
        String boostername = getConfig().getString("Boosters." + type.name() + ".type");
        int multi = getConfig().getInt("Boosters." + type.name() + ".multiplier");
        if (isBoosted(type) && isBoosterNotStackable(type)) {
            sender.sendMessage(getMessage("command-booster-active"));
            return;
        }
        if (isPlayerAlreadyUsingThisBooster(type, sender.getName())) {
            sender.sendMessage(getMessage("command-booster-this-type-active"));
            return;
        }
        if (isBoosterMaxed(type, multi)) {
            sender.sendMessage(getMessage("command-booster-maxed"));
            return;
        }

        Bukkit.broadcastMessage(
                getMessage("booster-activated").replace("{player}", sender.getName()).replace("{type}", boostername));
        for (Player online : Bukkit.getOnlinePlayers()) {
            online.playSound(online.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
        }
        activeBoosters.add(new Booster(sender.getUniqueId(), this, type, multi));

    }

    public void tryActivateBooster(Player activator, BoosterType type) {
        String boostername = getConfig().getString("Boosters." + type.name() + ".type");
        int multi = getConfig().getInt("Boosters." + type.name() + ".multiplier");
        if (isBoosted(type) && isBoosterNotStackable(type)) {
            activator.sendMessage(getMessage("command-booster-active"));
            return;
        }
        if (isPlayerAlreadyUsingThisBooster(type, activator.getName())) {
            activator.sendMessage(getMessage("command-booster-this-type-active"));
            return;
        }
        if (isBoosterMaxed(type, multi)) {
            activator.sendMessage(getMessage("command-booster-maxed"));
            return;
        }
        String uuid = activator.getUniqueId().toString();
        boolean activate = false;
        if (storage.contains(uuid + "." + type.name())) {
            int amount = storage.getInt(uuid + "." + type.name());
            int subtracted = amount - 1;
            if (subtracted < 1) {
                storage.set(uuid + "." + type.name(), null);
                activate = true;
            } else {
                storage.set(uuid + "." + type.name(), subtracted);
                activate = true;
            }
            try {
                storage.save(storagef);
            } catch (IOException e) {
                activator.sendMessage(getMessage("command-error-save"));
                activate = false;
                e.printStackTrace();
            }
        } else {
            activator.sendMessage(getMessage("command-no-booster"));
        }
        if (activate) {
            Bukkit.broadcastMessage(getMessage("booster-activated").replace("{player}", activator.getName())
                    .replace("{type}", boostername));
            for (Player online : Bukkit.getOnlinePlayers()) {
                online.playSound(online.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
            }
            activeBoosters.add(new Booster(activator.getUniqueId(), this, type, multi));
        }
    }

    private boolean isPlayerAlreadyUsingThisBooster(BoosterType type, String name) {
        for (Booster b : activeBoosters) {
            if (b.getType() == type) {
                if (b.getPlayerName().equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isBoosterMaxed(BoosterType type, int multi) {
        int max = getConfig().getInt("Boosters." + type.name() + ".max-active-multiplier");
        return (getMultiplier(type) + multi) > max;
    }

    private boolean isBoosterNotStackable(BoosterType type) {
        return !getConfig().getBoolean("Boosters." + type.name() + ".canStack");
    }

    public Integer getBoosterAmount(Player player, BoosterType type) {
        String uuid = player.getUniqueId().toString();
        if (storage.contains(uuid + "." + type.name())) {
            int stored = storage.getInt(uuid + "." + type.name());
            debug("The value " + uuid + "." + type.name() + " was fetched to be " + stored);
            return stored;
        }
        debug("The player " + player.getName() + " does not have the type " + type.name());
        return 0;
    }

    public void debug(String string) {
        if (getConfig().getBoolean("debug")) {
            getLogger().warning("[Debug] " + string);
        }
    }

    public int getMultiplier(BoosterType type) {
        int booster = 0;
        for (Booster b : activeBoosters) {
            if (b.getType() == type) {
                booster += b.getMultiplier();
            }
        }
        int max = getConfig().getInt("Boosters." + type.name() + ".max-active-multiplier");
        if (booster > max) {
            booster = max;
        }
        if (booster == 0) {
            booster = 1;
        }
        return booster;
    }

    public boolean isBoosted(BoosterType type) {
        for (Booster b : activeBoosters) {
            if (b.getType() == type) {
                return true;
            }
        }
        return false;
    }

    private String getWhoIsBoosting(BoosterType type) {
        String name = getMessage("server");
        for (Booster b : activeBoosters) {
            if (b.getType() == type) {
                if (name.equalsIgnoreCase(getMessage("server"))) {
                    name = b.getPlayerName();
                } else {
                    return getMessage("multiple-players");
                }
            }
        }
        return name;
    }

    public String getMultiplierName(BoosterType type) {
        int multi = getMultiplier(type);
        if (messages.contains("Name-of-the-multiplier." + multi)) {
            return messages.getString("Name-of-the-multiplier." + multi);
        }
        return messages.getString("Name-of-the-multiplier.other");
    }

    private String getTimeLeft(BoosterType type) {
        Booster firstOneToRunOut = null;
        Long time = 1000L * getConfig().getInt("Boosters." + type.name() + ".time");
        for (Booster b : activeBoosters) {
            if (b.getType() == type) {
                if (b.getRawTimeLeft() < time) {
                    firstOneToRunOut = b;
                    time = b.getRawTimeLeft();
                }
            }
        }
        if (firstOneToRunOut == null) {
            return getMessage("not-active");
        }
        return firstOneToRunOut.getTimeLeft();
    }

    public boolean isBoosterEnabled(BoosterType type) {
        return getConfig().getBoolean("Boosters." + type.name() + ".enabled");
    }


    public String getMessage(String path) {
        if (!messages.contains(path)) {
            debug("Messages file does not contain: " + path);
            return "§4Error: §cMissing message §4" + path;
        }
        return ChatColor.translateAlternateColorCodes('&', messages.getString(path));
    }

    public Material getGuiMaterial(String path) {
        if (!messages.contains(path)) {
            debug("Messages file does not contain material: " + path);
            return Material.BEDROCK;
        }
        try {
            return Material.valueOf(messages.getString(path).toUpperCase());
        } catch (Exception ex) {
            debug("Messages file contained an invalid material for: " + path);
            return Material.BEDROCK;
        }
    }

    public Integer getGuiAmount(String path) {
        if (!messages.contains(path)) {
            debug("Messages file does not contain amount: " + path);
            return -1;
        }
        try {
            return messages.getInt(path);
        } catch (Exception ex) {
            debug("Messages file contained an invalid amount for: " + path);
            return -1;
        }
    }

    public Short getGuiData(String path) {
        if (!messages.contains(path)) {
            debug("Messages file does not contain data: " + path);
            return 0;
        }
        try {
            return (short) messages.getInt(path);
        } catch (Exception ex) {
            debug("Messages file contained an invalid durability (data) for: " + path);
            return 0;
        }
    }

    public String getGuiName(String path) {
        if (!messages.contains(path)) {
            debug("Messages file does not contain item name: " + path);
            return "§4Error: §cMissing name for " + path;
        }
        return getMessage(path);
    }

    public List<String> getGuiLore(String path) {
        List<String> lore = new ArrayList<>();
        if (!messages.contains(path)) {
            lore.add("§7Invalid lore from " + path);
            debug("Messages file does not contain item lore: " + path);
            return lore;
        }
        List<String> retrieved = messages.getStringList(path);
        for (String s : retrieved) {
            lore.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        return lore;
    }

    public Boolean getGuiGlowing(String path) {
        if (!messages.contains(path)) {
            debug("Messages file does not contain item glow: " + path);
            return false;
        }
        try {
            return messages.getBoolean(path);
        } catch (Exception ex) {
            debug("Messages file contained an invalid boolean for: " + path);
            return false;
        }
    }
}
