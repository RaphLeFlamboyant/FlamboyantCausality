package me.flamboyant.causality.consequences;

import me.flamboyant.configurable.parameters.AParameter;
import me.flamboyant.configurable.parameters.EnumParameter;
import me.flamboyant.configurable.parameters.IntParameter;
import me.flamboyant.utils.Common;
import me.flamboyant.causality.TriggerType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.LootTables;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class SpawnLootFromLootTableConsequence extends ATwistConsequence {

    private List<LootTables> chestLootTables = Arrays.asList(LootTables.ABANDONED_MINESHAFT,
            LootTables.BURIED_TREASURE,
            LootTables.DESERT_PYRAMID,
            LootTables.END_CITY_TREASURE,
            LootTables.IGLOO_CHEST,
            LootTables.JUNGLE_TEMPLE,
            LootTables.JUNGLE_TEMPLE_DISPENSER,
            LootTables.NETHER_BRIDGE,
            LootTables.PILLAGER_OUTPOST,
            LootTables.BASTION_TREASURE,
            LootTables.BASTION_OTHER,
            LootTables.BASTION_BRIDGE,
            LootTables.BASTION_HOGLIN_STABLE,
            LootTables.RUINED_PORTAL,
            LootTables.SHIPWRECK_MAP,
            LootTables.SHIPWRECK_SUPPLY,
            LootTables.SHIPWRECK_TREASURE,
            LootTables.SIMPLE_DUNGEON,
            LootTables.SPAWN_BONUS_CHEST,
            LootTables.STRONGHOLD_CORRIDOR,
            LootTables.STRONGHOLD_CROSSING,
            LootTables.STRONGHOLD_LIBRARY,

            LootTables.UNDERWATER_RUIN_BIG,
            LootTables.UNDERWATER_RUIN_SMALL,
            LootTables.VILLAGE_ARMORER,
            LootTables.VILLAGE_BUTCHER,
            LootTables.VILLAGE_CARTOGRAPHER,
            LootTables.VILLAGE_DESERT_HOUSE,
            LootTables.VILLAGE_FISHER,
            LootTables.VILLAGE_FLETCHER,
            LootTables.VILLAGE_MASON,
            LootTables.VILLAGE_PLAINS_HOUSE,
            LootTables.VILLAGE_SAVANNA_HOUSE,
            LootTables.VILLAGE_SHEPHERD,
            LootTables.VILLAGE_SNOWY_HOUSE,
            LootTables.VILLAGE_TAIGA_HOUSE,
            LootTables.VILLAGE_TANNERY,
            LootTables.VILLAGE_TEMPLE,
            LootTables.VILLAGE_TOOLSMITH,
            LootTables.VILLAGE_WEAPONSMITH,
            LootTables.WOODLAND_MANSION,
            LootTables.ANCIENT_CITY_ICE_BOX,
            LootTables.ANCIENT_CITY);


    private static final int initialLuckToUse = 3;
    private IntParameter luckToUse = new IntParameter(Material.GOLD_INGOT, "Loot luck", "Loot luck", initialLuckToUse, 0, 5);
    private static final int initialLootingToUse = 3;
    private IntParameter lootingToUse = new IntParameter(Material.DIAMOND, "Looting bonus", "Looting bonus", initialLootingToUse, 0, 5);
    private static final LootTables initialLootTableToUse = LootTables.END_CITY_TREASURE;
    private EnumParameter<LootTables> lootTableToUse = new EnumParameter<LootTables>(Material.CHEST, "Loot table", "Loot table", chestLootTables.toArray(new LootTables[0]), initialLootTableToUse);

    @Override
    public void fillParameters(List<AParameter> parameterList) {
        parameterList.add(luckToUse);
        parameterList.add(lootingToUse);
        parameterList.add(lootTableToUse);
    }

    @Override
    public void resetParameters() {
        luckToUse.setValue(initialLuckToUse);
        lootingToUse.setValue(initialLootingToUse);
        lootTableToUse.setSelectedValue(initialLootTableToUse);
    }

    public void setLuckToUse(int luckToUseValue) {
        luckToUse.setValue(luckToUseValue);
    }

    public void setLootingToUse(int lootingToUseValue) {
        lootingToUse.setValue(lootingToUseValue);
    }

    public void setLootTableToUse(LootTables lootTableToUseValue) {
        lootTableToUse.setSelectedValue(lootTableToUseValue);
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

        if (difficulty < 0.1f)
            setLootTableToUse(LootTables.SPAWN_BONUS_CHEST);
        else if (difficulty < 0.2f)
            setLootTableToUse(LootTables.SHIPWRECK_SUPPLY);
        else if (difficulty < 0.3f)
            setLootTableToUse(LootTables.PILLAGER_OUTPOST);
        else if (difficulty < 0.4f)
            setLootTableToUse(LootTables.RUINED_PORTAL);
        else if (difficulty < 0.5f)
            setLootTableToUse(LootTables.SHIPWRECK_SUPPLY);
        else if (difficulty < 0.6f) {
            List<LootTables> lt = Arrays.asList(LootTables.VILLAGE_TOOLSMITH, LootTables.VILLAGE_ARMORER, LootTables.VILLAGE_WEAPONSMITH);
            setLootTableToUse(lt.get(Common.rng.nextInt(lt.size())));
        }
        else if (difficulty < 0.7f){
            List<LootTables> lt = Arrays.asList(LootTables.WOODLAND_MANSION, LootTables.BURIED_TREASURE);
            setLootTableToUse(lt.get(Common.rng.nextInt(lt.size())));
        }
        else if (difficulty < 0.8f)
            setLootTableToUse(LootTables.ANCIENT_CITY);
        else if (difficulty < 0.9f)
            setLootTableToUse(LootTables.END_CITY_TREASURE);
        else if (difficulty < 0.95f)
            setLootTableToUse(LootTables.BASTION_TREASURE);
        else if (difficulty < 0.95f) {
            List<LootTables> lt = Arrays.asList(LootTables.BASTION_TREASURE,
                    LootTables.END_CITY_TREASURE,
                    LootTables.ANCIENT_CITY,
                    LootTables.WOODLAND_MANSION,
                    LootTables.STRONGHOLD_LIBRARY,
                    LootTables.BURIED_TREASURE);
            setLootTableToUse(lt.get(Common.rng.nextInt(lt.size())));
        }
    }

    private void doExecute(Location location) {
        LootTables lootTable = lootTableToUse.getSelectedValue();
        if (lootTable == LootTables.EMPTY) {
            lootTable = chestLootTables.get(Common.rng.nextInt(chestLootTables.size()));
        }

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
