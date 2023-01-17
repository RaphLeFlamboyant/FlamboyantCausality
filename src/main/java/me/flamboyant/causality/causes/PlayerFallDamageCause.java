package me.flamboyant.causality.causes;

import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerFallDamageCause extends EntityDamagedCause {
    public PlayerFallDamageCause() {
        lifeLossThreshold.setParameterName("Fall life loss");
        lifeLossThreshold.setDescription("Fall life loss");
    }

    @Override
    protected boolean checkRequirements(EntityDamageEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) return false;
        if (event.getCause() != EntityDamageEvent.DamageCause.FALL) return false;

        return true;
    }
}
