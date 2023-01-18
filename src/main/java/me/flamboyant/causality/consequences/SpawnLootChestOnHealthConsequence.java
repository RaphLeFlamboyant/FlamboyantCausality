package me.flamboyant.causality.consequences;

import me.flamboyant.utils.Common;
import me.flamboyant.causality.TriggerType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.LootTables;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class SpawnLootChestOnHealthConsequence extends ATwistConsequence {
    private int variation = 4;

    private HashMap<Integer, List<LootTables>> lootByDamages = new HashMap<Integer, List<LootTables>>() {{
        put(15, Arrays.asList(LootTables.JUNGLE_TEMPLE_DISPENSER, LootTables.PILLAGER_OUTPOST, LootTables.SHIPWRECK_MAP, LootTables.SPAWN_BONUS_CHEST));
        put(12, Arrays.asList(LootTables.IGLOO_CHEST, LootTables.SHIPWRECK_SUPPLY, LootTables.ANCIENT_CITY_ICE_BOX, LootTables.UNDERWATER_RUIN_BIG, LootTables.UNDERWATER_RUIN_SMALL, LootTables.VILLAGER, LootTables.VILLAGE_TAIGA_HOUSE));
        put(10, Arrays.asList(LootTables.ABANDONED_MINESHAFT, LootTables.DESERT_PYRAMID, LootTables.SHIPWRECK_TREASURE, LootTables.SIMPLE_DUNGEON, LootTables.STRONGHOLD_CORRIDOR, LootTables.STRONGHOLD_CROSSING, LootTables.VILLAGE_ARMORER, LootTables.VILLAGE_TOOLSMITH, LootTables.VILLAGE_WEAPONSMITH));
        put(7, Arrays.asList(LootTables.BURIED_TREASURE, LootTables.JUNGLE_TEMPLE, LootTables.BASTION_BRIDGE, LootTables.BASTION_OTHER, LootTables.BASTION_HOGLIN_STABLE, LootTables.NETHER_BRIDGE, LootTables.WOODLAND_MANSION));
        put(3, Arrays.asList(LootTables.END_CITY_TREASURE, LootTables.STRONGHOLD_LIBRARY, LootTables.BASTION_TREASURE, LootTables.ANCIENT_CITY));
    }};

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

    private void doExecute(LivingEntity entity) {
        Location entityLocation = entity.getLocation();
        Location groundLocation = new Location(entityLocation.getWorld(), entityLocation.getBlockX(), entityLocation.getBlockY() - 1, entityLocation.getBlockZ());

        if (groundLocation.getBlock().getType() == Material.CHEST) return;

        int playerHealth = (int) entity.getHealth() + Common.rng.nextInt(variation) - (variation / 2);

        Integer targetHp = null;
        for (Integer hp : lootByDamages.keySet().stream().sorted(Comparator.comparingInt(o -> -o)).collect(Collectors.toList())) {
            if (hp < playerHealth) break;
            targetHp = hp;
        }

        if (targetHp == null) return;
        List<LootTables> tables = lootByDamages.get(targetHp);
        LootTable selectedTable = tables.get(Common.rng.nextInt(tables.size())).getLootTable();

        groundLocation.getBlock().setType(Material.CHEST);
        Chest chest = (Chest) groundLocation.getBlock().getState();
        Inventory chestInventory = chest.getBlockInventory();
        if (!chestInventory.isEmpty()) return;

        LootContext context =
                new LootContext.Builder(chest.getLocation())
                        .luck(3)
                        .lootingModifier(3)
                        .build();

        selectedTable.fillInventory(chestInventory, Common.rng, context);
    }
}
