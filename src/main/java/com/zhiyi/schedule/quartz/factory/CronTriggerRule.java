package com.zhiyi.schedule.quartz.factory;

import lombok.Data;
import org.quartz.CronTrigger;
import org.quartz.impl.triggers.AbstractTrigger;
import org.quartz.impl.triggers.CronTriggerImpl;

import java.util.TimeZone;

@Data
public class CronTriggerRule implements TriggerRule<CronTrigger> {

    /**
     * the cron expression for this trigger.
     */
    private String cronExpression;
    /**
     * the time zone for this trigger's cron expression.
     */
    private TimeZone timeZone;
    /**
     * Associate a specific calendar with this cron trigger.
     */
    private String calendarName;

    @Override
    public Class getTriggerClass() {
        return CronTrigger.class;
    }

    @Override
    public AbstractTrigger initializeTrigger() throws Exception {
        CronTriggerImpl trigger = new CronTriggerImpl();
        trigger.setCronExpression(cronExpression);
        trigger.setTimeZone(timeZone);
        trigger.setCalendarName(calendarName);
        return trigger;
    }

}
