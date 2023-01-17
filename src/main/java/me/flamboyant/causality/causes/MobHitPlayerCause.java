package me.flamboyant.causality.causes;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class MobHitPlayerCause extends HitEntityCause {
    @Override
    protected boolean checkRequirements(EntityDamageByEntityEvent event) {
        if (event.getEntity().getType() != EntityType.PLAYER) return false;
        if (!(event.getDamager() instanceof Mob)) return false;
        return true;
    }
}
