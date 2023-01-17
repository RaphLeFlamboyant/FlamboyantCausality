package me.flamboyant.causality.consequences;

import me.flamboyant.common.parameters.AParameter;
import me.flamboyant.causality.TriggerType;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class ATwistConsequence {
    public abstract List<TriggerType> getAdmissibleTriggerTypes();

    public void executeOnTrigger() {}
    public void executeOnPlayer(Player player) {}
    public void executeOnActor(LivingEntity entity) {}
    public void executeOnTarget(LivingEntity entity) {}
    public void executeOnLocation(Location location) {}
    public void executeOnBlock(Block block) {}
    public void executeOnItem(ItemStack item) {}
    public void executeOnText(String text) {}

    public void fillParameters(List<AParameter> parameterList) {}
    public void resetParameters() {}

    public void setSettings(float difficulty, boolean isPositiveEffect) {}
}
