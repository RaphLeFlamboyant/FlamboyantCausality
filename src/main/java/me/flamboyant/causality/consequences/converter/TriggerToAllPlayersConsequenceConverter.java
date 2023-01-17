package me.flamboyant.causality.consequences.converter;

import me.flamboyant.causality.TriggerType;
import me.flamboyant.causality.consequences.ATwistConsequence;
import me.flamboyant.common.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class TriggerToAllPlayersConsequenceConverter extends ATwistConsequence {
    private ATwistConsequence consequence;

    public TriggerToAllPlayersConsequenceConverter(ATwistConsequence consequence) {
        this.consequence = consequence;
    }

    @Override
    public List<TriggerType> getAdmissibleTriggerTypes() {
        return Arrays.asList(TriggerType.FLAT);
    }

    @Override
    public void executeOnTrigger() {
        Bukkit.getLogger().info("Execute on all players");
        for (Player player : Common.server.getOnlinePlayers()) {
            consequence.executeOnPlayer(player);
        }
    }

    public ATwistConsequence getChild() {
        return consequence;
    }
}
