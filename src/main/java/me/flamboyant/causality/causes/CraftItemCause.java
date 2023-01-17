package me.flamboyant.causality.causes;

import me.flamboyant.common.utils.Common;
import me.flamboyant.causality.TriggerType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class CraftItemCause extends ATwistCause implements Listener {
    @Override
    protected void start() {
        Common.server.getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    protected void stop() {
        PlayerItemConsumeEvent.getHandlerList().unregister(this);
    }

    @Override
    public List<TriggerType> getTransmissibleTriggerTypes() {
        return Arrays.asList(TriggerType.FLAT, TriggerType.PLAYER, TriggerType.ITEM, TriggerType.LOCATION);
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player whoClicked = (Player) event.getWhoClicked();
        ItemStack cursorStack = event.getCursor();

        if (!event.isShiftClick() && cursorStack.getType() != Material.AIR && event.getInventory().getResult().getType() != cursorStack.getType())
            return;
        if (!event.isShiftClick() && cursorStack.getType() != Material.AIR && event.getInventory().getResult().getAmount() + cursorStack.getAmount() > cursorStack.getMaxStackSize())
            return;

        int realQuantity = 9999;
        if (event.isShiftClick()) {
            int factor = event.getInventory().getResult().getAmount();
            for (ItemStack item : event.getInventory().getMatrix()) {
                if (item != null) realQuantity = Math.min(realQuantity, item.getAmount() * factor);
            }
        } else {
            realQuantity = event.getInventory().getResult().getAmount();
        }

        // TODO ; gérer le cas inventaire full

        ItemStack itemCopy = new ItemStack(event.getInventory().getResult().getType(), realQuantity);
        causality.executeOnTrigger();
        causality.executeOnPlayer(whoClicked);
        causality.executeOnLocation(event.getWhoClicked().getLocation());
        causality.executeOnItem(itemCopy);
    }
}
