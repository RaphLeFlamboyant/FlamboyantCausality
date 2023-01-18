package me.flamboyant.causality.causes;

import me.flamboyant.configurable.parameters.AParameter;
import me.flamboyant.configurable.parameters.BooleanParameter;
import me.flamboyant.configurable.parameters.IntParameter;
import me.flamboyant.utils.Common;
import me.flamboyant.causality.TriggerType;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.Arrays;
import java.util.List;

public class BlockPlaceCause extends ATwistCause implements Listener {
    private static final int initialProbability = 100;
    private IntParameter probability = new IntParameter(Material.REDSTONE, "Bloc place trigger %", "EBloc place trigger %", initialProbability, 0, 100);
    private int initialCancelBlockPlace;
    private BooleanParameter cancelBlockPlace = new BooleanParameter(Material.DIRT, "Cancel block place", "Cancel block place");

    public BlockPlaceCause() {
        this(true);
    }

    public BlockPlaceCause(boolean cancelBlockPlaceByDefault) {
        initialCancelBlockPlace = cancelBlockPlaceByDefault ? 1 : 0;
    }

    public void setCancelBlockPlace(boolean mustCancelBlockPlace) {
        cancelBlockPlace.setValue(mustCancelBlockPlace ? 1 : 0);
    }
    public void setProbability(float probabilityValue) {
        probability.setValue((int) (probabilityValue * 100));
    }

    @Override
    public List<TriggerType> getTransmissibleTriggerTypes() {
        return Arrays.asList(TriggerType.FLAT, TriggerType.PLAYER, TriggerType.BLOCK, TriggerType.LOCATION);
    }

    @Override
    public void setSettings(float difficulty, boolean _) {
        setCancelBlockPlace(Common.rng.nextFloat() * 0.4f + difficulty > 0.5f);
    }

    @Override
    public void fillParameters(List<AParameter> parameterList) {
        parameterList.add(probability);
        parameterList.add(cancelBlockPlace);
    }

    @Override
    public void resetParameters() {
        probability.setValue(initialProbability);
        cancelBlockPlace.setValue(initialCancelBlockPlace);
    }

    @Override
    protected void start() {
        Common.server.getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    protected void stop() {
        BlockPlaceEvent.getHandlerList().unregister(this);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (Common.rng.nextFloat() < (probability.getValue() / 100f)) {
            if (cancelBlockPlace.getValue() > 0) event.setCancelled(true);
            causality.executeOnTrigger();
            causality.executeOnPlayer(event.getPlayer());
            causality.executeOnLocation(event.getBlock().getLocation());
            causality.executeOnBlock(event.getBlock());
        }
    }
}
