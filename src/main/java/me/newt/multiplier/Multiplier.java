package me.newt.multiplier;

public class Multiplier {

    /**
     * Defined in the database.
     */
    private final int id;
    private final MultiplierType type;
    private final int duration;
    private final int multiplier;

    /**
     * Used to keep track of how long this multiplier should last.
     */
    private long timeExpires;

    /**
     * Constructor.
     * @param id         In-database id for this multiplier.
     * @param type       In-database type for this multiplier.
     * @param duration   In-database duration for this multiplier.
     * @param multiplier In-database multiplier strength for this multiplier.
     */
    public Multiplier(int id, MultiplierType type, int duration, int multiplier) {
        this.id = id;
        this.type = type;
        this.duration = duration;
        this.multiplier = multiplier;
    }

    /**
     * Get the in-database ID of this multiplier.
     * @return The ID of the multiplier.
     */
    public int getId() {
        return id;
    }

    /**
     * Get the in-database MultiplierType of this multiplier.
     * @return The MultiplierType of the multiplier.
     */
    public MultiplierType getType() {
        return type;
    }

    /**
     * Get the in-database duration of this multiplier.
     * @return The duration of the multiplier.
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Get the in-database multiplier strength of this multiplier.
     * @return The multiplier strength of the multiplier.
     */
    public int getMultiplierStrength() {
        return multiplier;
    }

    /**
     * Get the time when this multiplier should expire.
     * @return The time when this multiplier should expire.
     */
    public long getTimeExpires() {
        return timeExpires;
    }

    /**
     * Set the time when this multiplier should expire.
     * @param timeExpires The time when this multiplier should expire.
     */
    public void setTimeExpires(long timeExpires) {
        this.timeExpires = timeExpires;
    }
}
