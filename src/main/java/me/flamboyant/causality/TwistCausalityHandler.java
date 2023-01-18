package me.flamboyant.causality;

import me.flamboyant.parameters.AParameter;
import me.flamboyant.causality.causes.ATwistCause;
import me.flamboyant.causality.consequences.ATwistConsequence;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TwistCausalityHandler {
    private List<ATwistCause> causes;
    private List<ATwistConsequence> consequences;
    private List<TriggerType> executeOn;
    private String name = "-";
    private boolean isRunning;

    public TwistCausalityHandler(List<ATwistCause> causes, List<ATwistConsequence> consequences, List<TriggerType> executeOn) {
        this.causes = causes;
        this.consequences = consequences;
        this.executeOn = executeOn;
    }

    public TwistCausalityHandler(List<ATwistCause> causes, List<ATwistConsequence> consequences, List<TriggerType> executeOn, String name) {
        this(causes, consequences, executeOn);
        this.name = name;
    }

    public String getName() { return name; }

    public boolean start() {
        // Cause can be running before (causalities can share the same causes) so we must attach only when asked
        boolean res = true;
        for (ATwistCause cause : causes) {
            res &= cause.attachAndStart(this);
        }

        if (!res) {
            for (ATwistCause cause : causes) {
                res &= cause.detachAndStop();
            }
        }

        isRunning = res;

        return res;
    }

    public boolean stop() {
        boolean res = true;
        for (ATwistCause cause : causes) {
            res &= cause.detachAndStop();
        }

        isRunning = false;

        return res;
    }
    public boolean isRunning() {
        return isRunning;
    }

    public void resetParameters() {
        for (ATwistConsequence consequence : consequences) consequence.resetParameters();
        for (ATwistCause cause : causes) cause.resetParameters();
    }

    public void executeOnTrigger() {
        if (executeOn.contains(TriggerType.FLAT))
            for (ATwistConsequence consequence : consequences) consequence.executeOnTrigger();
    }

    public void executeOnPlayer(Player player) {
        if (executeOn.contains(TriggerType.PLAYER))
            for (ATwistConsequence consequence : consequences) consequence.executeOnPlayer(player);
    }

    public void executeOnActor(LivingEntity entity) {
        if (executeOn.contains(TriggerType.ENTITY_ACTOR))
            for (ATwistConsequence consequence : consequences) consequence.executeOnActor(entity);
    }

    public void executeOnTarget(LivingEntity entity) {
        if (executeOn.contains(TriggerType.ENTITY_TARGET))
            for (ATwistConsequence consequence : consequences) consequence.executeOnTarget(entity);
    }

    public void executeOnLocation(Location location) {
        if (executeOn.contains(TriggerType.LOCATION))
            for (ATwistConsequence consequence : consequences) consequence.executeOnLocation(location);
    }

    public void executeOnBlock(Block block) {
        if (executeOn.contains(TriggerType.BLOCK))
            for (ATwistConsequence consequence : consequences) consequence.executeOnBlock(block);
    }

    public void executeOnItem(ItemStack item) {
        if (executeOn.contains(TriggerType.ITEM))
            for (ATwistConsequence consequence : consequences) consequence.executeOnItem(item);
    }

    public void executeOnText(String text) {
        if (executeOn.contains(TriggerType.TEXT))
            for (ATwistConsequence consequence : consequences) consequence.executeOnText(text);
    }

    public void fillParameters(List<AParameter> parameterList) {
        for (ATwistCause cause : causes) {
            cause.fillParameters(parameterList);
        }

        for (ATwistConsequence consequence : consequences) {
            consequence.fillParameters(parameterList);
        }
    }
}
