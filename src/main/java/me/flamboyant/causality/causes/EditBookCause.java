package me.flamboyant.causality.causes;

import me.flamboyant.common.utils.Common;
import me.flamboyant.causality.TriggerType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;

import java.util.Arrays;
import java.util.List;

public class EditBookCause extends ATwistCause implements Listener {
    @Override
    protected void start() {
        Common.server.getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    protected void stop() {
        PlayerEditBookEvent.getHandlerList().unregister(this);
    }

    @Override
    public List<TriggerType> getTransmissibleTriggerTypes() {
        return Arrays.asList(TriggerType.FLAT, TriggerType.PLAYER, TriggerType.LOCATION);
    }

    @EventHandler
    public void onPlayerEditBook(PlayerEditBookEvent event) {
        causality.executeOnTrigger();
        causality.executeOnPlayer(event.getPlayer());
        causality.executeOnLocation(event.getPlayer().getLocation());
    }
}
