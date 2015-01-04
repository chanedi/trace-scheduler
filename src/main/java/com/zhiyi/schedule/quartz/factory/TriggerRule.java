package com.zhiyi.schedule.quartz.factory;

import org.quartz.impl.triggers.AbstractTrigger;

public interface TriggerRule<T> {

    public Class<T> getTriggerClass();

    /**
     * 创建trigger并初始化特殊参数
     * @return
     * @throws Exception
     */
    public AbstractTrigger initializeTrigger() throws Exception;

}
