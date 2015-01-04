package com.zhiyi.schedule.quartz.factory;

import com.zhiyi.schedule.Job;
import com.zhiyi.schedule.quartz.QuartzJob;
import lombok.Setter;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.triggers.AbstractTrigger;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.Constants;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.Map;

/**
 * Created by chanedi on 2014/12/18.
 */
public class TriggerFactoryBean implements FactoryBean<Trigger>, BeanNameAware, InitializingBean {

    @Setter
    private String mainTableName;
    /**
     * the trigger's name.
     */
    @Setter
    private String name;
    /**
     * the trigger's group.
     */
    @Setter
    private String group;
    private JobDetail jobDetail;
    @Setter
    private JobDataMap jobDataMap = new JobDataMap();
    /**
     * a specific start time for the trigger.
     * <p>Note that a dynamically computed {@link #setStartDelay} specification
     * overrides a static timestamp set here.
     */
    @Setter
    private Date startTime;
    private long startDelay = 0;
    /**
     * the priority of this trigger.
     */
    @Setter
    private int priority;
    /**
     * a misfire instruction for this trigger.
     */
    @Setter
    private int misfireInstruction;
    /**
     * Associate a textual description with this trigger.
     */
    @Setter
    private String description;
    @Setter
    private String beanName;
    @Setter
    private String jobClass;
    /**
     * 线程池大小
     */
    @Setter
    private int threadCount = 1;
    /**
     * 多线程运行时的超时设置
     */
    @Setter
    private long timeoutMinutesForMultiThread = 60;
    @Setter
    private boolean allowRetry = false;
    @Setter
    private TriggerRule triggerRule;
    private Class<? extends Trigger> triggerClass;
    private AbstractTrigger trigger;

    /**
     * Register objects in the JobDataMap via a given Map.
     * <p>These objects will be available to this Trigger only,
     * in contrast to objects in the JobDetail's data map.
     * @param jobDataAsMap Map with String keys and any objects as values
     * (for example Spring-managed beans)
     */
    public void setJobDataAsMap(Map<String, ?> jobDataAsMap) {
        this.jobDataMap.putAll(jobDataAsMap);
    }

    /**
     * Set the start delay in milliseconds.
     * <p>The start delay is added to the current system time (when the bean starts)
     * to control the start time of the trigger.
     */
    public void setStartDelay(long startDelay) {
        Assert.isTrue(startDelay >= 0, "Start delay cannot be negative");
        this.startDelay = startDelay;
    }

    /**
     * Set the misfire instruction via the name of the corresponding
     * constant in the {@link org.quartz.SimpleTrigger} class.
     * Default is {@code MISFIRE_INSTRUCTION_SMART_POLICY}.
     * @see org.quartz.SimpleTrigger#MISFIRE_INSTRUCTION_FIRE_NOW
     * @see org.quartz.SimpleTrigger#MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_EXISTING_COUNT
     * @see org.quartz.SimpleTrigger#MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT
     * @see org.quartz.SimpleTrigger#MISFIRE_INSTRUCTION_RESCHEDULE_NOW_WITH_EXISTING_REPEAT_COUNT
     * @see org.quartz.SimpleTrigger#MISFIRE_INSTRUCTION_RESCHEDULE_NOW_WITH_REMAINING_REPEAT_COUNT
     * @see org.quartz.Trigger#MISFIRE_INSTRUCTION_SMART_POLICY
     */
    public void setMisfireInstructionName(String constantName) {
        this.misfireInstruction = new Constants(triggerClass).asNumber(constantName).intValue();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // 参数验证
        Assert.notNull(this.triggerRule);
        Assert.notNull(this.mainTableName);
        Class<?> jClass = Class.forName(jobClass);
        Assert.isAssignable(Job.class, jClass);

        if (this.name == null) {
            this.name = this.beanName;
        }
        if (this.group == null) {
            this.group = Scheduler.DEFAULT_GROUP;
        }
        if (this.startDelay > 0 || this.startTime == null) {
            this.startTime = new Date(System.currentTimeMillis() + this.startDelay);
        }
        this.triggerClass = this.triggerRule.getTriggerClass();

        // jobDetail
        JobDetailImpl jdi = new JobDetailImpl();
        jdi.setName(this.name);
        jdi.setGroup(this.group);
        jdi.setJobClass(QuartzJob.class);
        jdi.setJobDataMap(this.jobDataMap);
//        jdi.setDurability(true);
        jdi.setDescription(this.description);
        this.jobDetail = jdi;

        // jobDataMap
        jobDataMap.put("jobDetail", jdi);
        jobDataMap.put(QuartzJob.JOB_CLASS, jClass);
        jobDataMap.put(QuartzJob.MAIN_TABLE_NAME, mainTableName);
        jobDataMap.put(QuartzJob.THREAD_COUNT, threadCount);
        jobDataMap.put(QuartzJob.TIMEOUT, timeoutMinutesForMultiThread);
        jobDataMap.put(QuartzJob.ALLOW_RETRY, allowRetry);

        // trigger
        this.trigger = triggerRule.initializeTrigger();
        trigger.setName(this.name);
        trigger.setGroup(this.group);
        trigger.setJobKey(this.jobDetail.getKey());
        trigger.setJobDataMap(this.jobDataMap);
        trigger.setStartTime(this.startTime);
        trigger.setPriority(this.priority);
        trigger.setMisfireInstruction(this.misfireInstruction);
        trigger.setDescription(this.description);
    }

    @Override
    public Trigger getObject() throws Exception {
        return this.trigger;
    }

    @Override
    public Class<?> getObjectType() {
        return this.triggerClass;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
