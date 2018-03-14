package me.theblockbender.xpboost.util;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.theblockbender.xpboost.Main;

public class Booster {

    private UUID owner;
    private String name;
    private Integer multiplier;
    private Long started_at;
    private Integer time;
    private Main main;
    private BoosterType type;

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
        time = config.getInt("Boosters." + type.name() + ".time");
        this.type = type;
    }

    public String getPlayerName() {
        return name;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(owner);
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
            return 0L;
        }
        return timeLeft;
    }

    public Integer getMultiplier() {
        return multiplier;
    }

    public BoosterType getType() {
        return type;
    }

}
