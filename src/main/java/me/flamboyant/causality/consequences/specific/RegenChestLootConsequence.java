package me.flamboyant.causality.consequences.specific;

import me.flamboyant.causality.TriggerType;
import me.flamboyant.causality.consequences.ATwistConsequence;
import me.flamboyant.common.utils.Common;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class RegenChestLootConsequence extends ATwistConsequence {
    private static final List<Material> chests = Arrays.asList(Material.CHEST, Material.CHEST_MINECART, Material.BARREL);
    private HashMap<String, LootTable> chestTable = new HashMap<>();

    @Override
    public void resetParameters() {
        chestTable.clear();
    }

    @Override
    public List<TriggerType> getAdmissibleTriggerTypes() {
        return Arrays.asList(TriggerType.BLOCK);
    }

    @Override
    public void executeOnBlock(Block block) {
        if (!chests.contains(block.getType())) return;
        Chest chest = (Chest) block.getState();
        Inventory chestInventory = chest.getBlockInventory();
        LootTable lootTable = chest.getLootTable();
        String chestLocationString = chest.getLocation().toString();

        if (lootTable == null) {
            if (!chestTable.containsKey(chestLocationString)) return;
            else lootTable = chestTable.get(chestLocationString);
        }
        if (!chestTable.containsKey(chestLocationString)) {
            chestTable.put(chestLocationString, lootTable);
        }

        chestInventory.clear();

        LootContext context =
                new LootContext.Builder(chest.getLocation())
                        .luck(3)
                        .lootingModifier(3)
                        .build();

        lootTable.fillInventory(chestInventory, Common.rng, context);
    }
}
