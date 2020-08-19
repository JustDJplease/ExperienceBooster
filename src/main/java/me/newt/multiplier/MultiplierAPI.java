package me.newt.multiplier;

import org.bukkit.scheduler.BukkitScheduler;

import java.util.*;

public class MultiplierAPI {

    private final MultiplierPlugin multiplierPlugin;
    private final BukkitScheduler scheduler;
    private final Map<UUID, List<Multiplier>> multipliers;
    private final List<Multiplier> activeMultipliers;

    /**
     * Constructor.
     * @param multiplierPlugin Instance of the main class.
     */
    public MultiplierAPI(MultiplierPlugin multiplierPlugin) {
        this.multiplierPlugin = multiplierPlugin;
        this.scheduler = multiplierPlugin.getServer().getScheduler();
        this.multipliers = new HashMap<>();
        this.activeMultipliers = new ArrayList<>();
    }

    /**
     * Get a list of multipliers owned by a player.
     * @param uuid The UUID of the player.
     * @return List of multipliers. May be empty the first few ticks after joining.
     */
    public List<Multiplier> getMultipliers(UUID uuid) {
        List<Multiplier> list = multipliers.get(uuid);
        if (list != null) {
            return multipliers.get(uuid);
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Give a multiplier to a player.
     * @param multiplier The multiplier to give.
     * @param uuid       The UUID of the player who should receive the multiplier.
     */
    public void giveMultiplier(Multiplier multiplier, UUID uuid) {
        List<Multiplier> list = multipliers.get(uuid);
        list.add(multiplier);
        multipliers.put(uuid, list);
        multiplierPlugin.getDatabaseAPI().addMultiplier(uuid, multiplier);
        // TODO notify the receiver if they are online. (DISPLAY, MESSAGE, SOUNDS ETC)
    }

    /**
     * Remove a multiplier (after it has been activated or removed by an admin).
     * @param id ID of the multiplier.
     */
    public void removeMultiplier(int id) {
        // Remove from database.
        multiplierPlugin.getDatabaseAPI().removeMultiplier(id);

        // Remove from memory list.
        Multiplier alreadyLoadedObject = null;
        UUID uuidWithLoadedMultiplier = null;
        for (Map.Entry<UUID, List<Multiplier>> entry : multipliers.entrySet()) {
            for (Multiplier multiplier : entry.getValue()) {
                if (multiplier.getId() == id) {
                    alreadyLoadedObject = multiplier;
                    uuidWithLoadedMultiplier = entry.getKey();
                    break;
                }
            }
            if (alreadyLoadedObject != null) break;
        }

        if (alreadyLoadedObject != null) {
            List<Multiplier> list = multipliers.get(uuidWithLoadedMultiplier);
            list.remove(alreadyLoadedObject);
            multipliers.put(uuidWithLoadedMultiplier, list);
        }
    }

    /**
     * Activate a multiplier.
     * @param multiplier Instance of the multiplier to activate.
     * @param inDatabase True if this multiplier is in the database. False if it has been activated via the start command.
     */
    public void activateMultiplier(Multiplier multiplier, boolean inDatabase) {
        // TODO activate the multiplier. (DISPLAY, MESSAGE, SOUNDS ETC)
        activeMultipliers.add(multiplier);
        if (inDatabase) removeMultiplier(multiplier.getId());
    }

    /**
     * End a multiplier that is currently active.
     * @param multiplier Instance of the active multiplier.
     */
    public void endActiveMultiplier(Multiplier multiplier) {
        // TODO end the multiplier. (DISPLAY, MESSAGE, SOUNDS ETC)
        activeMultipliers.remove(multiplier);
    }

    /**
     * End all multipliers currently active.
     */
    public void endAllActiveMultipliers() {
        List<Multiplier> toStop = new ArrayList<>(activeMultipliers);
        toStop.forEach(this::endActiveMultiplier);
    }

    /**
     * End all multipliers currently active of a certain type.
     * @param type The type of multiplier to end.
     */
    public void endAllActiveMultipliersOfType(MultiplierType type) {
        List<Multiplier> toStop = new ArrayList<>();
        activeMultipliers.forEach(multiplier -> {
            if (multiplier.getType() == type) toStop.add(multiplier);
        });
        toStop.forEach(this::endActiveMultiplier);
    }

    /**
     * (IS RAN ASYNC) Load a freshly joined player's multipliers into memory.
     * @param uuid UUID of the player.
     */
    public void loadMultipliersAsync(UUID uuid) {
        scheduler.runTaskAsynchronously(multiplierPlugin, () -> multipliers.put(uuid, multiplierPlugin.getDatabaseAPI().getMultipliers(uuid)));
    }

    /**
     * CALLBACK FUNCTION. For internal use only. Updates a multiplier's ID after it has been added to the database.
     * @param uuid       UUID of the player owning this multiplier.
     * @param multiplier Instance of this multiplier.
     * @param id         New ID of this multiplier.
     */
    public void updateID(UUID uuid, Multiplier multiplier, int id) {
        List<Multiplier> list = multipliers.get(uuid);
        list.remove(multiplier);
        multiplier.setId(id);
        list.add(multiplier);
        multipliers.put(uuid, list);
    }
}
