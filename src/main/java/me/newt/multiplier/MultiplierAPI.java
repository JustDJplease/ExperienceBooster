package me.newt.multiplier;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MultiplierAPI {

    private Map<UUID, List<Multiplier>> multipliers;
    // TODO LOAD this upon start & reload!

    public List<Multiplier> getMultipliers(UUID uuid){
        return multipliers.get(uuid);
    }

    public void activateMultiplier(Multiplier multiplier){

    }
}
