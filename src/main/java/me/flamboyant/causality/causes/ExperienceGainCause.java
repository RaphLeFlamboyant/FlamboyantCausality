package me.flamboyant.causality.causes;

import me.flamboyant.common.utils.Common;
import me.flamboyant.causality.TriggerType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

import java.util.Arrays;
import java.util.List;

public class ExperienceGainCause extends ATwistCause implements Listener {
    @Override
    public List<TriggerType> getTransmissibleTriggerTypes() {
        return Arrays.asList(TriggerType.FLAT, TriggerType.PLAYER, TriggerType.LOCATION);
    }

    @Override
    protected void start() {
        Common.server.getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    protected void stop() {
        PlayerExpChangeEvent.getHandlerList().unregister(this);
    }

    @EventHandler
    public void onPlayerExpChange(PlayerExpChangeEvent event) {
        causality.executeOnTrigger();
        causality.executeOnPlayer(event.getPlayer());
        causality.executeOnLocation(event.getPlayer().getLocation());
    }
}
