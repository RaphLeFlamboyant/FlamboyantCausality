package me.flamboyant.causality.consequences;

import me.flamboyant.common.parameters.AParameter;
import me.flamboyant.common.parameters.IntParameter;
import me.flamboyant.common.utils.Common;
import me.flamboyant.causality.TriggerType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class TeleportPlayerInAirsConsequence extends ATwistConsequence {
    private static final int initialProbability = 100;
    private IntParameter probability = new IntParameter(Material.REDSTONE, "Teleport probability", "Teleport probability", initialProbability, 0, 100);

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
        return Arrays.asList(TriggerType.ENTITY_ACTOR, TriggerType.ENTITY_TARGET, TriggerType.PLAYER);
    }

    @Override
    public void executeOnActor(LivingEntity actor) {
        doTeleport(actor);
    }

    @Override
    public void executeOnTarget(LivingEntity target) {
        doTeleport(target);
    }

    @Override
    public void executeOnPlayer(Player player) {
        doTeleport(player);
    }

    @Override
    public void setSettings(float difficulty, boolean _) {
        setProbability(0.3f + difficulty * 0.7f);
    }

    private void doTeleport(LivingEntity ety) {
        if (Common.rng.nextFloat() < (probability.getValue() / 100f)) {
            Location loc = ety.getLocation();
            if (ety.getWorld().getName().contains("nether")) {
                int fluctuation = Common.rng.nextInt(30);
                Location newLoc = new Location(loc.getWorld(), loc.getBlockX(), 124 - fluctuation, loc.getBlockZ());
                Location headLoc = new Location(loc.getWorld(), loc.getBlockX(), 124 - fluctuation + 1, loc.getBlockZ());

                headLoc.getBlock().setType(Material.AIR);
                newLoc.getBlock().setType(Material.AIR);
                ety.teleport(newLoc);
            }
            else {
                ety.teleport(new Location(loc.getWorld(), loc.getBlockX(), 318, loc.getBlockZ()));
            }
        }
    }
}
