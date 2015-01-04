package com.zhiyi.schedule.quartz.factory;

import lombok.Data;
import org.quartz.SimpleTrigger;
import org.quartz.impl.triggers.AbstractTrigger;
import org.quartz.impl.triggers.SimpleTriggerImpl;

@Data
public class SimpleTriggerRule implements TriggerRule<SimpleTrigger> {

    /**
     * the interval between execution times of this trigger.
     */
    private long repeatInterval;
    /**
     * the number of times this trigger is supposed to fire.
     * <p>Default is to repeat indefinitely.
     */
    private int repeatCount = -1;

    @Override
    public Class<SimpleTrigger> getTriggerClass() {
        return SimpleTrigger.class;
    }

    @Override
    public AbstractTrigger initializeTrigger() throws Exception {
        SimpleTriggerImpl trigger = new SimpleTriggerImpl();
        trigger.setRepeatInterval(repeatInterval);
        trigger.setRepeatCount(repeatCount);
        return trigger;
    }

}
