package me.flamboyant.causality.causes;

import me.flamboyant.configurable.parameters.AParameter;
import me.flamboyant.configurable.parameters.IntParameter;
import me.flamboyant.utils.Common;
import me.flamboyant.causality.TriggerType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Arrays;
import java.util.List;

public class EntityDamagedCause extends ATwistCause implements Listener {
    protected static final int initialLifeLossThreshold = 6;
    protected IntParameter lifeLossThreshold = new IntParameter(Material.LEATHER_BOOTS, "Life loss Threshold", "Life loss Threshold", initialLifeLossThreshold, 1, 18);

    public void setLifeLossThreshold(int threshold) {
        lifeLossThreshold.setValue(threshold);
    }

    @Override
    public List<TriggerType> getTransmissibleTriggerTypes() {
        return Arrays.asList(TriggerType.FLAT, TriggerType.ENTITY_TARGET, TriggerType.PLAYER, TriggerType.LOCATION);
    }

    @Override
    public void setSettings(float difficulty, boolean isPositiveEffect) {
        if (isPositiveEffect)
            setLifeLossThreshold(2);
        else
            setLifeLossThreshold(18 - (int) (16 * difficulty));
    }

    @Override
    public void fillParameters(List<AParameter> parameterList) {
        parameterList.add(lifeLossThreshold);
    }

    @Override
    public void resetParameters() {
        lifeLossThreshold.setValue(initialLifeLossThreshold);
    }

    @Override
    protected void start() {
        Common.server.getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    protected void stop() {
        EntityDamageEvent.getHandlerList().unregister(this);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageEvent event) {
        if (event instanceof EntityDamageByEntityEvent) return;
        if (!checkRequirements(event)) return;
        if (event.getFinalDamage() < lifeLossThreshold.getValue()) return;

        Bukkit.getScheduler().runTaskLater(Common.plugin, () -> {
            causality.executeOnTrigger();
            if (event.getEntity() instanceof Player)
                causality.executeOnActor((Player) event.getEntity());
            if (event.getEntity() instanceof LivingEntity)
                causality.executeOnTarget((LivingEntity) event.getEntity());
            causality.executeOnLocation(event.getEntity().getLocation());
            }, 1L);
    }

    protected boolean checkRequirements(EntityDamageEvent event) {
        return true;
    }
}
