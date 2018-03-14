package me.theblockbender.xpboost.util;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.theblockbender.xpboost.Main;

public class Booster {

    private UUID owner;
    private String name = "Unknown";
    private Integer multiplier = 2;
    private Long started_at;
    private String word = "Double";
    private Integer time = 600;
    private Main main;
    private BoosterType type = BoosterType.Minecraft;

    public Booster(UUID uuid, Main main, BoosterType type, int multiplier) {
        this.main = main;
        owner = uuid;
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) {
            name = main.getMessage("unknown");
        } else {
            name = player.getName();
        }
        FileConfiguration config = main.getConfig();
        this.multiplier = multiplier;
        started_at = System.currentTimeMillis();
        word = parseWord(multiplier);
        time = config.getInt("Boosters." + type.name() + ".time");
        this.type = type;
    }

    private String parseWord(int multi) {
        FileConfiguration config = main.getConfig();
        if (config.contains("Name-of-the-multiplier." + multi)) {
            return config.getString("Name-of-the-multiplier." + multi);
        }
        return config.getString("Name-of-the-multiplier.other");
    }

    public String getPlayerName() {
        return name;
    }

    public Player getPlayer() {
        Player player = Bukkit.getPlayer(owner);
        return player;
    }

    public String getTimeLasting() {
        Long timeLasting = time * 1000L;
        return main.utilTime.translateTime(timeLasting);
    }

    public String getTimeLeft() {
        Long timeLeft = getRawTimeLeft();
        if (timeLeft <= 0) {
            main.endActiveBooster(this);
            return "0.0 " + main.getMessage("time-second") + main.getMessage("time-multiple");
        }
        return main.utilTime.translateTime(timeLeft);
    }

    public Long getRawTimeLeft() {
        Long timeLasting = time * 1000L;
        Long timeLeft = (started_at + timeLasting) - System.currentTimeMillis();
        if (timeLeft <= 0) {
            return 0l;
        }
        return timeLeft;
    }

    public String getMultiplierWord() {
        return word;
    }

    public Integer getMultiplier() {
        return multiplier;
    }

    public BoosterType getType() {
        return type;
    }

}
