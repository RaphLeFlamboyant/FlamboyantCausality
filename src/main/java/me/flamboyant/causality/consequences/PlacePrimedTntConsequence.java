package me.flamboyant.causality.consequences;

import me.flamboyant.parameters.AParameter;
import me.flamboyant.parameters.IntParameter;
import me.flamboyant.utils.Common;
import me.flamboyant.causality.TriggerType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class PlacePrimedTntConsequence extends ATwistConsequence {
    private static final int initialProbability = 25;
    private IntParameter probability = new IntParameter(Material.GUNPOWDER, "TNT spawn probability", "TNT spawn probability", initialProbability, 0, 100);

    @Override
    public void fillParameters(List<AParameter> parameterList) {
        parameterList.add(probability);
    }

    @Override
    public void resetParameters() {
        probability.setValue(initialProbability);
    }

    public void setProbability(float probabilityValue) {
        probability.setValue((int) (probabilityValue * 100));
    }

    @Override
    public List<TriggerType> getAdmissibleTriggerTypes() {
        return Arrays.asList(TriggerType.LOCATION, TriggerType.ENTITY_ACTOR, TriggerType.ENTITY_TARGET, TriggerType.PLAYER, TriggerType.BLOCK);
    }

    @Override
    public void executeOnActor(LivingEntity entity) {
        doExecute(entity.getLocation());
    }

    @Override
    public void executeOnTarget(LivingEntity entity) {
        doExecute(entity.getLocation());
    }

    @Override
    public void executeOnPlayer(Player player) {
        doExecute(player.getLocation());
    }

    @Override
    public void executeOnLocation(Location location) {
        doExecute(location);
    }

    @Override
    public void executeOnBlock(Block block) {
        doExecute(block.getLocation());
    }

    @Override
    public void setSettings(float difficulty, boolean _) {
        setProbability(0.3f + difficulty * 0.6f);
    }

    private void doExecute(Location location) {
        if (Common.rng.nextFloat() < (probability.getValue() / 100f)) {
            location.getWorld().spawnEntity(location, EntityType.PRIMED_TNT);
        }
    }
}
