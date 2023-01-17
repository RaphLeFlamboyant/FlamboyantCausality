package me.flamboyant.causality.consequences;

import me.flamboyant.common.parameters.AParameter;
import me.flamboyant.common.parameters.IntParameter;
import me.flamboyant.common.utils.Common;
import me.flamboyant.causality.TriggerType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.List;

public class IncreaseVelocityConsequence extends ATwistConsequence {
    private static final int initialPower = 20;
    private IntParameter power = new IntParameter(Material.SLIME_BLOCK, "Velocity power", "Velocity power", initialPower, 0, 80);

    public void setPower(int powerValue) {
        power.setValue(powerValue);
    }

    @Override
    public void fillParameters(List<AParameter> parameterList) {
        parameterList.add(power);
    }

    @Override
    public void resetParameters() {
        power.setValue(initialPower);
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
        setPower(6 + (int) (44f * difficulty));
    }

    private void doExecute(LivingEntity entity) {
        Bukkit.getScheduler().runTaskLater(Common.plugin, () -> {
            Vector velocity = entity.getVelocity();
            entity.setVelocity(velocity.multiply(power.getValue() / 4f));
        }, 1L);
    }
}
