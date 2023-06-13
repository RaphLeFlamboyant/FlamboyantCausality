package me.flamboyant.causality.causes;

import me.flamboyant.configurable.parameters.IntParameter;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerFallDamageCause extends EntityDamagedCause {
    public PlayerFallDamageCause() {
        lifeLossThreshold = new IntParameter(Material.LEATHER_BOOTS, "Fall life loss", "Fall life loss", initialLifeLossThreshold, 1, 18);
    }

    @Override
    protected boolean checkRequirements(EntityDamageEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) return false;
        if (event.getCause() != EntityDamageEvent.DamageCause.FALL) return false;

        return true;
    }
}
