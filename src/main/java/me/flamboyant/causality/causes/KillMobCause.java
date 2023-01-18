package me.flamboyant.causality.causes;

import me.flamboyant.utils.Common;
import me.flamboyant.causality.TriggerType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Arrays;
import java.util.List;

public class KillMobCause extends ATwistCause implements Listener {
    @Override
    public List<TriggerType> getTransmissibleTriggerTypes() {
        return Arrays.asList(TriggerType.FLAT, TriggerType.PLAYER, TriggerType.ENTITY_TARGET, TriggerType.LOCATION);
    }

    @Override
    protected void start() {
        Common.server.getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    protected void stop() {
        EntityDeathEvent.getHandlerList().unregister(this);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity deadEntity = event.getEntity();
        Player killer = deadEntity.getKiller();

        if (killer == null) return;

        causality.executeOnTrigger();
        causality.executeOnPlayer(killer);
        causality.executeOnTarget(deadEntity);
        causality.executeOnLocation(deadEntity.getLocation());
    }
}
