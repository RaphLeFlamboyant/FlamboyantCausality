package me.flamboyant.causality.consequences.converter;

import me.flamboyant.causality.TriggerType;
import me.flamboyant.causality.consequences.ATwistConsequence;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class LivingEntityToRainConsequenceConverter extends ATwistConsequence {
    private ATwistConsequence consequence;

    public LivingEntityToRainConsequenceConverter(ATwistConsequence consequence) {
        this.consequence = consequence;
    }

    @Override
    public List<TriggerType> getAdmissibleTriggerTypes() {
        return Arrays.asList(TriggerType.PLAYER, TriggerType.ENTITY_TARGET, TriggerType.ENTITY_ACTOR);
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

    public ATwistConsequence getChild() {
        return consequence;
    }

    private void doExecute(LivingEntity entity) {
        Location location = entity.getLocation();
        World world = location.getWorld();
        Location tntSpawnLocation = location;
        for (int y = location.getBlockY() + 15; y > location.getBlockY(); y--)
        {
            tntSpawnLocation = new Location(world, location.getBlockX(), y, location.getBlockZ());
            Block block = tntSpawnLocation.getBlock();
            if (block.isEmpty() || block.isLiquid()) break;
        }
        consequence.executeOnLocation(tntSpawnLocation);
    }
}
