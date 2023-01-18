package me.flamboyant.causality.consequences;

import me.flamboyant.parameters.AParameter;
import me.flamboyant.parameters.IntParameter;
import me.flamboyant.utils.Common;
import me.flamboyant.causality.TriggerType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class SpawnHostileMobConsequence extends ATwistConsequence {
    private List<EntityType> spawnableEntities = Arrays.asList(
            EntityType.BAT,
            EntityType.BEE,
            EntityType.BLAZE,
            EntityType.CAVE_SPIDER,
            EntityType.CREEPER,
            EntityType.DROWNED,
            EntityType.ELDER_GUARDIAN,
            EntityType.ENDERMAN,
            EntityType.ENDERMITE,
            EntityType.EVOKER,
            EntityType.GHAST,
            EntityType.GIANT,
            EntityType.GUARDIAN,
            EntityType.HOGLIN,
            EntityType.HUSK,
            EntityType.ILLUSIONER,
            EntityType.MAGMA_CUBE,
            EntityType.PHANTOM,
            EntityType.PIGLIN,
            EntityType.PIGLIN_BRUTE,
            EntityType.PILLAGER,
            EntityType.POLAR_BEAR,
            EntityType.PUFFERFISH,
            EntityType.RAVAGER,
            EntityType.SHULKER,
            EntityType.SKELETON,
            EntityType.SLIME,
            EntityType.SPIDER,
            EntityType.STRAY,
            EntityType.VEX,
            EntityType.VINDICATOR,
            EntityType.WITCH,
            EntityType.WITHER_SKELETON,
            EntityType.ZOGLIN,
            EntityType.ZOMBIE,
            EntityType.ZOMBIE_VILLAGER,
            EntityType.ZOMBIFIED_PIGLIN
    );

    private static final int initialProbability = 100;
    private IntParameter probability = new IntParameter(Material.REDSTONE, "Mob spawn probability", "Mob spawn probability", initialProbability, 0, 100);
    private static final int initialMobNumber = 1;
    private IntParameter mobNumber = new IntParameter(Material.ZOMBIE_HEAD, "Mob spawn number", "Mob spawn number", initialMobNumber, 0, 4);

    @Override
    public void fillParameters(List<AParameter> parameterList) {
        parameterList.add(probability);
        parameterList.add(mobNumber);
    }

    @Override
    public void resetParameters() {
        probability.setValue(initialProbability);
        mobNumber.setValue(initialMobNumber);
    }

    public void setMobNumber(int mobNumberValue) {
        Bukkit.getLogger().info("Spawn mob nbr " + mobNumberValue);
        mobNumber.setValue(mobNumberValue);
    }

    public void setProbability(float probabilityValue) {
        Bukkit.getLogger().info("Spawn mob probability " + probabilityValue);
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
        if (difficulty < 0.2) {
            setMobNumber(1);
            setProbability((float) (0.5 + 0.5 * difficulty / 0.2));
            return;
        }

        setProbability(1);
        if (difficulty > 0.7)
            setMobNumber(4);
        else
            setMobNumber(1 + (int) (3 * ((difficulty - 0.2) / 0.5)));
    }

    private void doExecute(Location location) {
        if (Common.rng.nextFloat() < (probability.getValue() / 100f)) {
            for (int i = 0; i < mobNumber.getValue(); i++) {
                EntityType type = spawnableEntities.get(Common.rng.nextInt(spawnableEntities.size()));
                location.getWorld().spawnEntity(location, type);
            }
        }
    }
}
