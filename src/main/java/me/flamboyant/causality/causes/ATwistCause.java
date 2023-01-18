package me.flamboyant.causality.causes;

import me.flamboyant.configurable.parameters.AParameter;
import me.flamboyant.causality.TriggerType;
import me.flamboyant.causality.TwistCausalityHandler;

import java.util.List;

public abstract class ATwistCause {
    protected TwistCausalityHandler causality;

    public final boolean attachAndStart(TwistCausalityHandler causality) {
        if (this.causality != null) return false;
        this.causality = causality;
        start();
        return true;
    }

    public final boolean detachAndStop() {
        if (this.causality == null) return false;
        this.causality = null;
        stop();
        return true;
    }

    public abstract List<TriggerType> getTransmissibleTriggerTypes();
    public void setSettings(float difficulty, boolean isPositiveEffect) {}

    public void fillParameters(List<AParameter> parameterList) {}
    public void resetParameters() {}

    protected abstract void start();
    protected abstract void stop();
}
