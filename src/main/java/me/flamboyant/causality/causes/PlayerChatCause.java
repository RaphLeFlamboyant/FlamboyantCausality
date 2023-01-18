package me.flamboyant.causality.causes;

import me.flamboyant.configurable.parameters.AParameter;
import me.flamboyant.utils.Common;
import me.flamboyant.causality.TriggerType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Arrays;
import java.util.List;

public class PlayerChatCause extends ATwistCause implements Listener {
    @Override
    public List<TriggerType> getTransmissibleTriggerTypes() {
        return Arrays.asList(TriggerType.FLAT, TriggerType.PLAYER, TriggerType.TEXT, TriggerType.ENTITY_ACTOR);
    }

    @Override
    public void fillParameters(List<AParameter> parameterList) {
    }

    @Override
    public void resetParameters() {
    }

    @Override
    protected void start() {
        Common.server.getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    protected void stop() {
        AsyncPlayerChatEvent.getHandlerList().unregister(this);
    }

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Bukkit.getScheduler().runTaskLater(Common.plugin, () -> {
            for(Player player : event.getRecipients()) {
                causality.executeOnPlayer(player);
            }
            causality.executeOnText(event.getMessage());
            causality.executeOnTrigger();
            causality.executeOnActor(event.getPlayer());
        }, 1L);
    }
}
