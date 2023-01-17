package me.flamboyant.causality.causes;

import me.flamboyant.common.utils.Common;
import me.flamboyant.causality.TriggerType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Arrays;
import java.util.List;

public class HitEntityCause extends ATwistCause implements Listener {
    @Override
    public List<TriggerType> getTransmissibleTriggerTypes() {
        return Arrays.asList(TriggerType.FLAT, TriggerType.ENTITY_ACTOR, TriggerType.ENTITY_TARGET, TriggerType.PLAYER, TriggerType.LOCATION);
    }

    @Override
    protected void start() {
        Common.server.getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    protected void stop() {
        EntityDamageByEntityEvent.getHandlerList().unregister(this);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!checkRequirements(event)) return;

        causality.executeOnTrigger();
        if (event.getEntity() instanceof Player)
            causality.executeOnPlayer((Player) event.getEntity());
        if (event.getDamager() instanceof LivingEntity)
            causality.executeOnActor((LivingEntity) event.getDamager());
        if (event.getEntity() instanceof LivingEntity)
            causality.executeOnTarget((LivingEntity) event.getEntity());
        causality.executeOnLocation(event.getEntity().getLocation());
    }

    protected boolean checkRequirements(EntityDamageByEntityEvent event) {
        return true;
    }
}
