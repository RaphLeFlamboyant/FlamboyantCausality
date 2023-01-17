package me.flamboyant.causality.consequences;

import me.flamboyant.common.parameters.AParameter;
import me.flamboyant.common.parameters.IntParameter;
import me.flamboyant.common.utils.Common;
import me.flamboyant.causality.TriggerType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class ExplodeConsequence extends ATwistConsequence {
    private static final int initialProbability = 25;
    private IntParameter probability = new IntParameter(Material.REDSTONE, "Explosion probability", "Explosion probability", initialProbability, 0, 100);
    private static final int initialPower = 10;
    private IntParameter power = new IntParameter(Material.CREEPER_HEAD, "Explosion power", "Explosion power", initialPower, 0, 100);

    @Override
    public void fillParameters(List<AParameter> parameterList) {
        parameterList.add(probability);
        parameterList.add(power);
    }

    @Override
    public void resetParameters() {
        probability.setValue(initialProbability);
        power.setValue(initialPower);
    }

    public void setPower(float powerValue) {
        power.setValue((int) (powerValue * 100));
    }

    public void setProbability(float probabilityValue) {
        Bukkit.getLogger().info("Explosion probability " + probabilityValue);
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
        setPower(0.05f + (difficulty > 0.05f ? 0.05f : 0) + 0.9f * difficulty);
    }

    private void doExecute(Location location) {
        if (Common.rng.nextFloat() < (probability.getValue() / 100f)) {
            location.getWorld().createExplosion(location, power.getValue() / 10f, true);
        }
    }
}
