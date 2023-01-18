package me.flamboyant.causality.consequences;

import me.flamboyant.configurable.parameters.AParameter;
import me.flamboyant.configurable.parameters.IntParameter;
import me.flamboyant.utils.Common;
import me.flamboyant.causality.TriggerType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class DuplicateEntityConsequence extends ATwistConsequence {
    private static final int initialProbability = 100;
    private IntParameter probability = new IntParameter(Material.REDSTONE, "Entity duplication probability", "Entity duplication probability", initialProbability, 0, 100);
    private static final int initialEntityNumber = 1;
    private IntParameter entityNumber = new IntParameter(Material.ZOMBIE_HEAD, "Duplicate entity number", "Duplicate entity number", initialEntityNumber, 0, 4);

    @Override
    public void fillParameters(List<AParameter> parameterList) {
        parameterList.add(probability);
        parameterList.add(entityNumber);
    }

    @Override
    public void resetParameters() {
        probability.setValue(initialProbability);
        entityNumber.setValue(initialEntityNumber);
    }

    public void setProbability(float probabilityValue) {
        probability.setValue((int) (probabilityValue * 100));
    }

    public void setEntityNumber(int entityNumberValue) {
        entityNumber.setValue(entityNumberValue);
    }

    @Override
    public List<TriggerType> getAdmissibleTriggerTypes() {
        return Arrays.asList(TriggerType.ENTITY_TARGET, TriggerType.ENTITY_ACTOR);
    }

    @Override
    public void executeOnActor(LivingEntity entity) {
        doExecute(entity);
    }

    @Override
    public void executeOnTarget(LivingEntity entity) {
        doExecute(entity);
    }

    @Override
    public void setSettings(float difficulty, boolean _) {
        if (difficulty < 0.2) {
            setEntityNumber(1);
            setProbability((float) (0.5 + 0.5 * difficulty / 0.2));
            return;
        }

        setProbability(1);
        if (difficulty > 0.7)
            setEntityNumber(4);
        else
            setEntityNumber(1 + (int) (3 * ((difficulty - 0.2) / 0.5)));
    }

    private void doExecute(LivingEntity entity) {
        if (entity instanceof Player) return;

        if (Common.rng.nextFloat() < (probability.getValue() / 100f)) {
            Location location = entity.getLocation();
            for (int i = 0; i < entityNumber.getValue(); i++) {
                location.getWorld().spawnEntity(location, entity.getType());
            }
        }
    }
}
