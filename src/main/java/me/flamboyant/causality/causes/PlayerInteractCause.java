package me.flamboyant.causality.causes;

import me.flamboyant.utils.Common;
import me.flamboyant.causality.TriggerType;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerInteractCause extends ATwistCause implements Listener {
    private static final List<Action> leftClickAction = Arrays.asList(Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK);
    private static final List<Action> rightClickAction = Arrays.asList(Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK);

    private List<Action> activationActions = new ArrayList<>();
    private Material materialToInteractWith = null;
    private Material itemThatMustBeUsed = null;

    public PlayerInteractCause() {
    }

    public PlayerInteractCause(boolean isLeftClick) {
        if (isLeftClick) activationActions.addAll(leftClickAction);
        else activationActions.addAll(rightClickAction);
    }

    public PlayerInteractCause(boolean allowsPhysical, Material blockMaterial) {
        if (allowsPhysical) activationActions.add(Action.PHYSICAL);
        materialToInteractWith = blockMaterial;
    }

    public PlayerInteractCause(Material blockMaterial, Material itemInClick) {
        materialToInteractWith = blockMaterial;
        itemThatMustBeUsed = itemInClick;
    }

    public PlayerInteractCause(boolean isLeftClick, boolean allowsPhysical, Material blockMaterial) {
        this(isLeftClick);
        if (allowsPhysical) activationActions.add(Action.PHYSICAL);
        materialToInteractWith = blockMaterial;
    }

    public PlayerInteractCause(boolean isLeftClick, Material blockMaterial, Material itemInClick) {
        this(isLeftClick);
        materialToInteractWith = blockMaterial;
        itemThatMustBeUsed = itemInClick;
    }

    public void setActivationActions(List<Action> al) {
        activationActions.clear();
        activationActions.addAll(al);
    }

    public void setMaterialToInteractWith(Material material) {
        materialToInteractWith = material;
    }

    public void setItemThatMustBeUsed(Material material) {
        itemThatMustBeUsed = material;
    }

    @Override
    public List<TriggerType> getTransmissibleTriggerTypes() {
        return Arrays.asList(TriggerType.FLAT, TriggerType.PLAYER, TriggerType.LOCATION, TriggerType.BLOCK, TriggerType.ITEM);
    }

    @Override
    public void setSettings(float difficulty, boolean isPositiveEffect) {
        if (isPositiveEffect) {
            setActivationActions(Arrays.asList(Action.values()));
            List<Material> materials = Arrays.asList(Material.BELL, Material.BARREL, Material.DIAMOND_BLOCK, Material.BEDROCK, Material.JACK_O_LANTERN);
            setMaterialToInteractWith(materials.get(Common.rng.nextInt(materials.size())));
            setItemThatMustBeUsed(null);
            return;
        }
    }

    @Override
    protected void start() {
        Common.server.getPluginManager().registerEvents(this, Common.plugin);
    }

    @Override
    protected void stop() {
        PlayerInteractEvent.getHandlerList().unregister(this);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (activationActions.isEmpty() || !activationActions.contains(event.getAction())) return;
        if (materialToInteractWith != null && (event.getClickedBlock() == null || event.getClickedBlock().getType() != materialToInteractWith)) return;
        if (itemThatMustBeUsed != null && event.getItem().getType() != itemThatMustBeUsed) return;

        causality.executeOnTrigger();
        causality.executeOnPlayer(event.getPlayer());
        causality.executeOnLocation(event.getPlayer().getLocation());
        if (event.getClickedBlock() != null) causality.executeOnBlock(event.getClickedBlock());
        causality.executeOnItem(event.getItem());
    }
}
