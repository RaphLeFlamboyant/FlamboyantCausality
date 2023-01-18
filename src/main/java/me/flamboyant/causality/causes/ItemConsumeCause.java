package me.flamboyant.causality.causes;

import me.flamboyant.utils.Common;
import me.flamboyant.causality.TriggerType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.util.Arrays;
import java.util.List;

public class ItemConsumeCause extends ATwistCause implements Listener {
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
        PlayerItemConsumeEvent.getHandlerList().unregister(this);
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        causality.executeOnTrigger();
        causality.executeOnPlayer(event.getPlayer());
        causality.executeOnLocation(event.getPlayer().getLocation());
    }
}
