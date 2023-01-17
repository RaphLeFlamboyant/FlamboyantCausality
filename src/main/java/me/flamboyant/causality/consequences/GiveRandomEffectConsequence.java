package me.flamboyant.causality.consequences;

import me.flamboyant.common.parameters.AParameter;
import me.flamboyant.common.parameters.EnumParameter;
import me.flamboyant.common.parameters.IntParameter;
import me.flamboyant.common.utils.Common;
import me.flamboyant.causality.TriggerType;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

public class GiveRandomEffectConsequence extends ATwistConsequence {
    private static final int initialDuration = 5;
    private IntParameter duration = new IntParameter(Material.EXPERIENCE_BOTTLE, "Effect duration", "Effect duration (mn)", initialDuration, 0, 60);
    private static final int initialPower = 1;
    private IntParameter power = new IntParameter(Material.DIAMOND_SWORD, "Effect power", "Effect power", initialPower, 0, 10);
    private static final EffectOrientation initialOrientation = EffectOrientation.BOTH;
    private EnumParameter<EffectOrientation> orientation = new EnumParameter<>(Material.CREEPER_HEAD, "Effect orientation", "Effect orientation", EffectOrientation.values(), initialOrientation);

    private List<PotionEffectType> goodEffectTypes = Arrays.asList(PotionEffectType.WATER_BREATHING,
            PotionEffectType.SPEED,
            PotionEffectType.SLOW_FALLING,
            PotionEffectType.REGENERATION,
            PotionEffectType.FIRE_RESISTANCE,
            PotionEffectType.NIGHT_VISION,
            PotionEffectType.JUMP,
            PotionEffectType.INVISIBILITY,
            PotionEffectType.INCREASE_DAMAGE,
            PotionEffectType.HEALTH_BOOST,
            PotionEffectType.HEAL,
            PotionEffectType.DAMAGE_RESISTANCE,
            PotionEffectType.CONDUIT_POWER,
            PotionEffectType.DOLPHINS_GRACE,
            PotionEffectType.FAST_DIGGING,
            PotionEffectType.GLOWING,
            PotionEffectType.HERO_OF_THE_VILLAGE,
            PotionEffectType.LUCK,
            PotionEffectType.SATURATION,
            PotionEffectType.ABSORPTION);
    private List<PotionEffectType> badEffectTypes = Arrays.asList(PotionEffectType.HARM,
            PotionEffectType.SLOW,
            PotionEffectType.POISON,
            PotionEffectType.WEAKNESS,
            PotionEffectType.BLINDNESS,
            PotionEffectType.LEVITATION,
            PotionEffectType.BAD_OMEN,
            PotionEffectType.CONFUSION,
            PotionEffectType.DARKNESS,
            PotionEffectType.HUNGER,
            PotionEffectType.SLOW_DIGGING,
            PotionEffectType.UNLUCK,
            PotionEffectType.WITHER);

    @Override
    public void fillParameters(List<AParameter> parameterList) {
        parameterList.add(duration);
        parameterList.add(power);
    }

    @Override
    public void resetParameters() {
        duration.setValue(initialDuration);
        power.setValue(initialPower);
    }

    public void setDuration(int durationValue) {
        power.setValue(durationValue);
    }

    public void setPower(int powerValue) {
        power.setValue(powerValue);
    }

    public void setOrientation(EffectOrientation o) {
        orientation.setSelectedValue(o);
    }

    @Override
    public List<TriggerType> getAdmissibleTriggerTypes() {
        return Arrays.asList(TriggerType.PLAYER, TriggerType.ENTITY_ACTOR, TriggerType.ENTITY_TARGET);
    }

    @Override
    public void executeOnPlayer(Player player) {
        giveEntityEffect(player);
    }

    @Override
    public void executeOnActor(LivingEntity entity) {
        giveEntityEffect(entity);
    }

    @Override
    public void executeOnTarget(LivingEntity entity) {
        giveEntityEffect(entity);
    }

    @Override
    public void setSettings(float difficulty, boolean isPositiveEffect) {
        setOrientation(isPositiveEffect ? EffectOrientation.GOOD : difficulty < 0.3 ? EffectOrientation.BOTH : EffectOrientation.BAD);
        setPower(1 + (int) (9f * difficulty));
        setDuration(1 + (int) (4 * difficulty));
    }

    private void giveEntityEffect(Entity entity) {
        if (!(entity instanceof LivingEntity)) return;
        giveEntityEffect((LivingEntity) entity);
    }

    private void giveEntityEffect(LivingEntity ety) {
        if (orientation.getSelectedValue() != EffectOrientation.BAD) {
            PotionEffectType selectedType = goodEffectTypes.get(Common.rng.nextInt(goodEffectTypes.size()));
            PotionEffect effect = new PotionEffect(selectedType, duration.getValue() * 60 * 20, power.getValue());
            ety.addPotionEffect(effect);
        }
        if (orientation.getSelectedValue() != EffectOrientation.GOOD) {
            PotionEffectType selectedType = badEffectTypes.get(Common.rng.nextInt(badEffectTypes.size()));
            PotionEffect effect = new PotionEffect(selectedType, duration.getValue() * 60 * 20, 1);
            ety.addPotionEffect(effect);
        }
    }
    
    public enum EffectOrientation {
        GOOD,
        BAD,
        BOTH
    }
}
