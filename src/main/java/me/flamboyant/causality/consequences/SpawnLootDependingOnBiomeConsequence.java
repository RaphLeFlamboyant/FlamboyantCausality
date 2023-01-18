package me.flamboyant.causality.consequences;

import me.flamboyant.parameters.AParameter;
import me.flamboyant.parameters.IntParameter;
import me.flamboyant.utils.Common;
import me.flamboyant.causality.TriggerType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.LootTables;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class SpawnLootDependingOnBiomeConsequence extends ATwistConsequence {
    private class BiomesToLootTables {
        public List<Biome> biomes;
        public List<LootTables> lootTables;

        public BiomesToLootTables(List<Biome> biomes, List<LootTables> lootTables) {
            this.biomes = biomes;
            this.lootTables = lootTables;
        }
    }

    private BiomesToLootTables badlandLootTables = new BiomesToLootTables(Arrays.asList(Biome.BADLANDS, Biome.ERODED_BADLANDS, Biome.WOODED_BADLANDS),
            Arrays.asList(LootTables.ABANDONED_MINESHAFT, LootTables.RUINED_PORTAL));
    private BiomesToLootTables jungleLootTables = new BiomesToLootTables(Arrays.asList(Biome.JUNGLE, Biome.SPARSE_JUNGLE, Biome.SPARSE_JUNGLE),
            Arrays.asList(LootTables.JUNGLE_TEMPLE, LootTables.RUINED_PORTAL, LootTables.JUNGLE_TEMPLE_DISPENSER));
    private BiomesToLootTables beachLootTables = new BiomesToLootTables(Arrays.asList(Biome.BEACH),
            Arrays.asList(LootTables.BURIED_TREASURE));
    private BiomesToLootTables seaLootTables = new BiomesToLootTables(Arrays.asList(Biome.OCEAN, Biome.COLD_OCEAN, Biome.DEEP_COLD_OCEAN, Biome.DEEP_OCEAN, Biome.DEEP_FROZEN_OCEAN, Biome.DEEP_LUKEWARM_OCEAN, Biome.FROZEN_OCEAN, Biome.LUKEWARM_OCEAN, Biome.WARM_OCEAN),
            Arrays.asList(LootTables.SHIPWRECK_MAP, LootTables.SHIPWRECK_TREASURE, LootTables.SHIPWRECK_SUPPLY, LootTables.UNDERWATER_RUIN_SMALL, LootTables.UNDERWATER_RUIN_BIG, LootTables.RUINED_PORTAL));
    private BiomesToLootTables forestsAndPlainsLootTables = new BiomesToLootTables(Arrays.asList(Biome.FOREST, Biome.BIRCH_FOREST, Biome.OLD_GROWTH_BIRCH_FOREST, Biome.PLAINS, Biome.SUNFLOWER_PLAINS, Biome.STONY_PEAKS, Biome.STONY_SHORE, Biome.MEADOW),
            Arrays.asList(LootTables.PILLAGER_OUTPOST,
                    LootTables.SPAWN_BONUS_CHEST,
                    LootTables.VILLAGE_ARMORER,
                    LootTables.VILLAGE_BUTCHER,
                    LootTables.VILLAGE_CARTOGRAPHER,
                    LootTables.VILLAGE_FISHER,
                    LootTables.VILLAGE_FLETCHER,
                    LootTables.VILLAGE_MASON,
                    LootTables.VILLAGE_SHEPHERD,
                    LootTables.VILLAGE_TANNERY,
                    LootTables.VILLAGE_TEMPLE,
                    LootTables.VILLAGE_TOOLSMITH,
                    LootTables.VILLAGE_WEAPONSMITH,
                    LootTables.VILLAGE_PLAINS_HOUSE));
    private BiomesToLootTables coldPlacesLootTables = new BiomesToLootTables(Arrays.asList(Biome.WINDSWEPT_FOREST, Biome.WINDSWEPT_HILLS, Biome.WINDSWEPT_GRAVELLY_HILLS, Biome.TAIGA, Biome.SNOWY_TAIGA, Biome.OLD_GROWTH_PINE_TAIGA, Biome.OLD_GROWTH_SPRUCE_TAIGA, Biome.SNOWY_BEACH, Biome.SNOWY_PLAINS, Biome.SNOWY_SLOPES, Biome.ICE_SPIKES, Biome.JAGGED_PEAKS, Biome.FROZEN_PEAKS, Biome.FROZEN_RIVER, Biome.GROVE),
            Arrays.asList(LootTables.PILLAGER_OUTPOST,
                    LootTables.SPAWN_BONUS_CHEST,
                    LootTables.IGLOO_CHEST,
                    LootTables.VILLAGE_ARMORER,
                    LootTables.VILLAGE_BUTCHER,
                    LootTables.VILLAGE_CARTOGRAPHER,
                    LootTables.VILLAGE_FISHER,
                    LootTables.VILLAGE_FLETCHER,
                    LootTables.VILLAGE_MASON,
                    LootTables.VILLAGE_SHEPHERD,
                    LootTables.VILLAGE_TANNERY,
                    LootTables.VILLAGE_TEMPLE,
                    LootTables.VILLAGE_TOOLSMITH,
                    LootTables.VILLAGE_WEAPONSMITH,
                    LootTables.VILLAGE_TAIGA_HOUSE));
    private BiomesToLootTables darkForestLootTables = new BiomesToLootTables(Arrays.asList(Biome.DARK_FOREST),
            Arrays.asList(LootTables.WOODLAND_MANSION));
    private BiomesToLootTables endLootTables = new BiomesToLootTables(Arrays.asList(Biome.END_BARRENS, Biome.END_HIGHLANDS, Biome.END_MIDLANDS, Biome.THE_END, Biome.SMALL_END_ISLANDS),
            Arrays.asList(LootTables.END_CITY_TREASURE, LootTables.STRONGHOLD_LIBRARY, LootTables.STRONGHOLD_CROSSING, LootTables.STRONGHOLD_CORRIDOR));
    private BiomesToLootTables desertLootTables = new BiomesToLootTables(Arrays.asList(Biome.DESERT),
            Arrays.asList(LootTables.DESERT_PYRAMID,
                    LootTables.VILLAGE_ARMORER,
                    LootTables.VILLAGE_BUTCHER,
                    LootTables.VILLAGE_CARTOGRAPHER,
                    LootTables.VILLAGE_FISHER,
                    LootTables.VILLAGE_FLETCHER,
                    LootTables.VILLAGE_MASON,
                    LootTables.VILLAGE_SHEPHERD,
                    LootTables.VILLAGE_TANNERY,
                    LootTables.VILLAGE_TEMPLE,
                    LootTables.VILLAGE_TOOLSMITH,
                    LootTables.VILLAGE_WEAPONSMITH,
                    LootTables.VILLAGE_DESERT_HOUSE));
    private BiomesToLootTables undergroundLootTables = new BiomesToLootTables(Arrays.asList(Biome.DRIPSTONE_CAVES, Biome.LUSH_CAVES),
            Arrays.asList(LootTables.SPAWN_BONUS_CHEST));
    private BiomesToLootTables deepDarkLootTables = new BiomesToLootTables(Arrays.asList(Biome.DEEP_DARK),
            Arrays.asList(LootTables.ANCIENT_CITY));
    private BiomesToLootTables netherLootTables = new BiomesToLootTables(Arrays.asList(Biome.NETHER_WASTES, Biome.BASALT_DELTAS, Biome.CRIMSON_FOREST, Biome.WARPED_FOREST, Biome.SOUL_SAND_VALLEY),
            Arrays.asList(LootTables.NETHER_BRIDGE, LootTables.BASTION_BRIDGE, LootTables.BASTION_TREASURE, LootTables.BASTION_OTHER, LootTables.BASTION_HOGLIN_STABLE));
    private BiomesToLootTables savannaLootTables = new BiomesToLootTables(Arrays.asList(Biome.SAVANNA, Biome.SAVANNA_PLATEAU, Biome.WINDSWEPT_SAVANNA),
            Arrays.asList(LootTables.DESERT_PYRAMID,
                    LootTables.VILLAGE_ARMORER,
                    LootTables.VILLAGE_BUTCHER,
                    LootTables.VILLAGE_CARTOGRAPHER,
                    LootTables.VILLAGE_FISHER,
                    LootTables.VILLAGE_FLETCHER,
                    LootTables.VILLAGE_MASON,
                    LootTables.VILLAGE_SHEPHERD,
                    LootTables.VILLAGE_TANNERY,
                    LootTables.VILLAGE_TEMPLE,
                    LootTables.VILLAGE_TOOLSMITH,
                    LootTables.VILLAGE_WEAPONSMITH,
                    LootTables.VILLAGE_SAVANNA_HOUSE));

    private HashMap<Biome, BiomesToLootTables> tablesByBiome = new HashMap<>();

    public SpawnLootDependingOnBiomeConsequence() {
        insertEntries(badlandLootTables);
        insertEntries(jungleLootTables);
        insertEntries(beachLootTables);
        insertEntries(seaLootTables);
        insertEntries(forestsAndPlainsLootTables);
        insertEntries(coldPlacesLootTables);
        insertEntries(darkForestLootTables);
        insertEntries(endLootTables);
        insertEntries(desertLootTables);
        insertEntries(undergroundLootTables);
        insertEntries(deepDarkLootTables);
        insertEntries(netherLootTables);
        insertEntries(savannaLootTables);
    }

    private void insertEntries(BiomesToLootTables btl) {
        for (Biome b : btl.biomes) {
            tablesByBiome.put(b, btl);
        }
    }

    private static final int initialLuckToUse = 3;
    private IntParameter luckToUse = new IntParameter(Material.GOLD_INGOT, "Loot luck", "Loot luck", initialLuckToUse, 0, 5);
    private static final int initialLootingToUse = 3;
    private IntParameter lootingToUse = new IntParameter(Material.DIAMOND, "Looting bonus", "Looting bonus", initialLootingToUse, 0, 5);

    @Override
    public void fillParameters(List<AParameter> parameterList) {
        parameterList.add(luckToUse);
        parameterList.add(lootingToUse);
    }

    @Override
    public void resetParameters() {
        luckToUse.setValue(initialLuckToUse);
        lootingToUse.setValue(initialLootingToUse);
    }

    public void setLuckToUse(int luckToUseValue) {
        luckToUse.setValue(luckToUseValue);
    }

    public void setLootingToUse(int lootingToUseValue) {
        lootingToUse.setValue(lootingToUseValue);
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
        setLootingToUse(1 + (int) (2.5f * difficulty));
        setLuckToUse(1 + (int) (2.5f * difficulty));
    }

    private void doExecute(Location location) {
        Biome biome = location.getBlock().getBiome();
        if (!tablesByBiome.containsKey(biome)) return;

        BiomesToLootTables btl = tablesByBiome.get(biome);
        LootTables lootTable = btl.lootTables.get(Common.rng.nextInt(btl.lootTables.size()));

        LootContext context =
                new LootContext.Builder(location)
                        .luck(luckToUse.getValue())
                        .lootingModifier(lootingToUse.getValue())
                        .build();

        LootTable lt = lootTable.getLootTable();
        Collection<ItemStack> items = lt.populateLoot(Common.rng, context);

        for (ItemStack item : items) {
            location.getWorld().dropItem(location, item);
        }
    }
}
