package me.flamboyant.causality.consequences;

import me.flamboyant.configurable.parameters.AParameter;
import me.flamboyant.configurable.parameters.IntParameter;
import me.flamboyant.utils.Common;
import me.flamboyant.causality.TriggerType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.List;

public class PushLivingEntityConsequence extends ATwistConsequence {
    private static final int initialPower = 100;
    private IntParameter power = new IntParameter(Material.ELYTRA, "Push power", "Push power", initialPower, 0, 100);

    @Override
    public void fillParameters(List<AParameter> parameterList) {
        parameterList.add(power);
    }

    @Override
    public void resetParameters() {
        power.setValue(initialPower);
    }

    public void setPower(int powerValue) {
        Bukkit.getLogger().info("Push power " + powerValue);
        power.setValue(powerValue);
    }

    @Override
    public List<TriggerType> getAdmissibleTriggerTypes() {
        return Arrays.asList(TriggerType.ENTITY_ACTOR, TriggerType.ENTITY_TARGET, TriggerType.PLAYER);
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
    public void executeOnPlayer(Player player) {
        doExecute(player);
    }

    @Override
    public void setSettings(float difficulty, boolean _) {
        setPower(25 + (int) (175 * difficulty));
    }

    private void doExecute(LivingEntity entity) {
        double factor = power.getValue() / 100.0;
        double pushY = (Common.rng.nextDouble() * 2.5 + 0.5);
        entity.setVelocity(new Vector((Common.rng.nextDouble() * 20 - 10), pushY, Common.rng.nextDouble() * 20 - 10).multiply(factor));
    }
}
