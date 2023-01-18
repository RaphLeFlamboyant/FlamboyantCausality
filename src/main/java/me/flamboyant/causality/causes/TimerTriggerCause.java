package me.flamboyant.causality.causes;

import me.flamboyant.configurable.parameters.AParameter;
import me.flamboyant.configurable.parameters.IntParameter;
import me.flamboyant.utils.Common;
import me.flamboyant.causality.TriggerType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitTask;

import java.util.Arrays;
import java.util.List;

public class TimerTriggerCause extends ATwistCause {
    private static final int initialInterval = 60;
    private IntParameter intervalInSeconds = new IntParameter(Material.CLOCK, "Trigger interval (sec)", "Trigger interval (sec)", initialInterval, 1, 600);
    private BukkitTask triggerTask;

    public void setIntervalInSeconds(int intervalValue) {
        intervalInSeconds.setValue(intervalValue);
    }

    @Override
    public List<TriggerType> getTransmissibleTriggerTypes() {
        return Arrays.asList(TriggerType.FLAT);
    }

    @Override
    public void setSettings(float difficulty, boolean _) {
        setIntervalInSeconds(30 - (int) (Common.rng.nextInt(29) * difficulty));
    }

    @Override
    public void fillParameters(List<AParameter> parameterList) {
        parameterList.add(intervalInSeconds);
    }

    @Override
    public void resetParameters() {
        intervalInSeconds.setValue(initialInterval);
    }

    @Override
    protected void start() {
        if (triggerTask != null) {
            stop();
        }

        triggerTask = Bukkit.getScheduler().runTaskTimer(Common.plugin, () -> {
            trigger();
        }, intervalInSeconds.getMaxValue() * 20L, intervalInSeconds.getMaxValue() * 20L);
    }

    @Override
    protected void stop() {
        if (triggerTask != null) {
            Bukkit.getScheduler().cancelTask(triggerTask.getTaskId());
            triggerTask = null;
        }
    }

    private void trigger() {
        causality.executeOnTrigger();
    }
}
