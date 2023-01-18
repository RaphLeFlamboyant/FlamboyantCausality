package me.flamboyant.causality.consequences.specific;

import me.flamboyant.causality.TriggerType;
import me.flamboyant.causality.consequences.ATwistConsequence;
import me.flamboyant.utils.Common;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.LootTables;

import java.util.*;
import java.util.stream.Collectors;

public class ChestPopulateByLootAltitude extends ATwistConsequence {
    private static final List<Material> chests = Arrays.asList(Material.CHEST, Material.CHEST_MINECART, Material.BARREL);
    private int variation = 40;

    private HashSet<String> alreadyGeneratedLocation = new HashSet<>();
    private HashMap<Integer, List<LootTables>> lootByAltitude = new HashMap<Integer, List<LootTables>>() {{
        put(125, Arrays.asList(LootTables.JUNGLE_TEMPLE_DISPENSER, LootTables.PILLAGER_OUTPOST, LootTables.SHIPWRECK_MAP, LootTables.SPAWN_BONUS_CHEST));
        put(170, Arrays.asList(LootTables.IGLOO_CHEST, LootTables.SHIPWRECK_SUPPLY, LootTables.UNDERWATER_RUIN_BIG, LootTables.UNDERWATER_RUIN_SMALL, LootTables.VILLAGER, LootTables.VILLAGE_TAIGA_HOUSE));
        put(220, Arrays.asList(LootTables.ABANDONED_MINESHAFT, LootTables.DESERT_PYRAMID, LootTables.SHIPWRECK_TREASURE, LootTables.SIMPLE_DUNGEON, LootTables.STRONGHOLD_CORRIDOR, LootTables.STRONGHOLD_CROSSING, LootTables.VILLAGE_ARMORER, LootTables.VILLAGE_TOOLSMITH, LootTables.VILLAGE_WEAPONSMITH));
        put(270, Arrays.asList(LootTables.BURIED_TREASURE, LootTables.WOODLAND_MANSION, LootTables.JUNGLE_TEMPLE, LootTables.BASTION_BRIDGE, LootTables.BASTION_OTHER, LootTables.BASTION_HOGLIN_STABLE, LootTables.NETHER_BRIDGE));
        put(310, Arrays.asList(LootTables.END_CITY_TREASURE, LootTables.STRONGHOLD_LIBRARY, LootTables.BASTION_TREASURE));
    }};


    @Override
    public void resetParameters() {
        alreadyGeneratedLocation.clear();
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
        if (!chestInventory.isEmpty()) return;

        String chestLocationString = chest.getLocation().toString();
        if (alreadyGeneratedLocation.contains(chestLocationString)) return;
        alreadyGeneratedLocation.add(chestLocationString);

        int selectedLevel = block.getLocation().getBlockY() + Common.rng.nextInt(variation) - (variation / 2);

        Integer targetYLevel = null;
        for (Integer levelY : lootByAltitude.keySet().stream().sorted().collect(Collectors.toList())) {
            if (levelY > selectedLevel) break;
            targetYLevel = levelY;
        }

        if (targetYLevel == null) return;
        List<LootTables> tables = lootByAltitude.get(targetYLevel);
        LootTable selectedTable = tables.get(Common.rng.nextInt(tables.size())).getLootTable();

        LootContext context =
                new LootContext.Builder(chest.getLocation())
                        .luck(3)
                        .lootingModifier(3)
                        .build();

        selectedTable.fillInventory(chestInventory, Common.rng, context);
    }
}
