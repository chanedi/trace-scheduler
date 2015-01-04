package com.zhiyi.schedule.model;

import com.zhiyi.schedule.base.model.EntityWithTime;
import lombok.Data;

import javax.persistence.Table;
import javax.persistence.Transient;

@Data
@Table(name = "TB_SCHE_TASK_INFO")
public class TaskInfo extends EntityWithTime {

    private Object id;
    private String jobName;
    private String jobClassName;
    private String mainTableName;
    private String jobData;
    private Integer dataCount = 0;
    private Integer processedCount = 0;
    private Integer failureCount = 0;
    private Boolean isFailed = false;
    private Boolean allowRetry;

    public void addProcessedCount() {
        processedCount++;
    }

    public void addFailureCount() {
        failureCount++;
    }

    public void minusFailureCount() {
        failureCount--;
    }

    @Transient
    public int getSuccessedCount() {
        return processedCount - failureCount;
    }

    public void checkIsFailed() {
        isFailed = (dataCount != getSuccessedCount());
    }
}
