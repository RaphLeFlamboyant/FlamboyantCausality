package me.flamboyant.causality.consequences;

import me.flamboyant.configurable.parameters.AParameter;
import me.flamboyant.configurable.parameters.EnumParameter;
import me.flamboyant.utils.Common;
import me.flamboyant.causality.TriggerType;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class EnchantConsequence extends ATwistConsequence {
    private static final EnchantmentSlot initialSlot = EnchantmentSlot.ALL;
    private EnumParameter<EnchantmentSlot> slot = new EnumParameter<>(Material.ANVIL, "Slot to enchant", "Slot to enchant", EnchantmentSlot.values(), initialSlot);

    private List<Enchantment> validEnchantments;

    @Override
    public void fillParameters(List<AParameter> parameterList) {
        parameterList.add(slot);
    }

    @Override
    public void resetParameters() {
        slot.setSelectedValue(initialSlot);
    }

    public EnchantConsequence() {
        validEnchantments = new ArrayList<>(Collections.unmodifiableList(Arrays.asList(Enchantment.values())).stream()
                .filter(e -> !e.equals(Enchantment.BINDING_CURSE) && !e.equals(Enchantment.VANISHING_CURSE))
                .collect(Collectors.toList()));
    }

    public void setEnchantmentSlot(EnchantmentSlot enchantmentSlot) {
        slot.setSelectedValue(enchantmentSlot);
    }

    @Override
    public List<TriggerType> getAdmissibleTriggerTypes() {
        return Arrays.asList(TriggerType.PLAYER);
    }

    @Override
    public void executeOnPlayer(Player player) {
        ArrayList<ItemStack> itemsToEnchant = retrieveItemList(player.getInventory());

        for (ItemStack item : itemsToEnchant) {
            addRandomSafeEnchantOnItem(item);
        }
    }

    @Override
    public void setSettings(float difficulty, boolean _) {
        List<EnchantConsequence.EnchantmentSlot> slots;
        if (difficulty < 0.33) {
            slots = Arrays.asList(EnchantConsequence.EnchantmentSlot.BOOTS, EnchantConsequence.EnchantmentSlot.HELMET, EnchantConsequence.EnchantmentSlot.LEFT_HAND, EnchantConsequence.EnchantmentSlot.RIGHT_HAND, EnchantConsequence.EnchantmentSlot.LEGGINGS, EnchantConsequence.EnchantmentSlot.CHESTPLATE);
        }
        else if (difficulty < 0.66) {
            slots = Arrays.asList(EnchantConsequence.EnchantmentSlot.ARMORS, EnchantConsequence.EnchantmentSlot.WEAPONS);
        }
        else {
            slots = Arrays.asList(EnchantConsequence.EnchantmentSlot.ALL);
        }
        setEnchantmentSlot(slots.get(Common.rng.nextInt(slots.size())));
    }

    private void addRandomSafeEnchantOnItem(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return;

        ArrayList<Enchantment> enchantments = new ArrayList<>(validEnchantments);
        Enchantment selectedEnchant;
        do {
            selectedEnchant = enchantments.get(Common.rng.nextInt(enchantments.size()));
            int level = item.getEnchantmentLevel(selectedEnchant);
            if (level < selectedEnchant.getMaxLevel()) {
                try {
                    item.addEnchantment(selectedEnchant, level + 1);
                }
                catch (IllegalArgumentException e) {}
                break;
            }
            else
                enchantments.remove(selectedEnchant);
        } while (enchantments.size() > 0);
    }

    private ArrayList<ItemStack> retrieveItemList(PlayerInventory inv) {
        ArrayList<ItemStack> itemsToEnchant = new ArrayList<>();

        switch (slot.getSelectedValue()) {
            case RIGHT_HAND:
                itemsToEnchant.add(inv.getItemInMainHand());
                break;
            case LEFT_HAND:
                itemsToEnchant.add(inv.getItemInOffHand());
                break;
            case CHESTPLATE:
                itemsToEnchant.add(inv.getChestplate());
                break;
            case LEGGINGS:
                itemsToEnchant.add(inv.getLeggings());
                break;
            case BOOTS:
                itemsToEnchant.add(inv.getBoots());
                break;
            case HELMET:
                itemsToEnchant.add(inv.getHelmet());
                break;
            case ARMORS:
                itemsToEnchant.add(inv.getChestplate());
                itemsToEnchant.add(inv.getLeggings());
                itemsToEnchant.add(inv.getBoots());
                itemsToEnchant.add(inv.getHelmet());
                break;
            case WEAPONS:
                itemsToEnchant.add(inv.getItemInMainHand());
                itemsToEnchant.add(inv.getItemInOffHand());
                break;
            case ALL:
                itemsToEnchant.add(inv.getChestplate());
                itemsToEnchant.add(inv.getLeggings());
                itemsToEnchant.add(inv.getBoots());
                itemsToEnchant.add(inv.getHelmet());
                itemsToEnchant.add(inv.getItemInMainHand());
                itemsToEnchant.add(inv.getItemInOffHand());
                break;
        }

        return itemsToEnchant;
    }

    public enum EnchantmentSlot {
        HELMET,
        CHESTPLATE,
        LEGGINGS,
        BOOTS,
        LEFT_HAND,
        RIGHT_HAND,
        ARMORS,
        WEAPONS,
        ALL
    }
}
