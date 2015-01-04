package com.zhiyi.schedule;

import chanedi.context.ContextUtils;
import chanedi.dao.complexQuery.CustomQueryParam;
import chanedi.dao.complexQuery.QueryParamBuilder;
import com.alibaba.fastjson.JSONObject;
import com.zhiyi.schedule.exception.InvalidIdException;
import com.zhiyi.schedule.model.TaskDetailInfo;
import com.zhiyi.schedule.model.TaskInfo;
import com.zhiyi.schedule.service.TaskDetailInfoService;
import com.zhiyi.schedule.service.TaskInfoService;
import lombok.Setter;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by chanedi on 2014/12/17.
 */
public abstract class TraceJob implements Job {

    protected Logger logger = Logger.getLogger(getClass().getName());
    /**
     * 每次取limit条数据执行任务。
     */
    protected static final int limit = 1000;
    /**
     * 是否补偿任务。
     */
    @Setter
    protected boolean isCompensate = false;
    /**
     * 任务执行针对的主表名
     */
    @Setter
    protected String mainTableName;
    @Setter
    protected String jobName;
    @Setter
    protected boolean allowRetry;
    @Setter
    protected JSONObject jobData;
    @Setter
    protected TaskInfo taskInfo;
    protected int dataCount;

    protected TaskInfoService taskInfoService;
    protected TaskDetailInfoService taskDetailInfoService;

    public TraceJob() {
        super();
        taskInfoService = (TaskInfoService) ContextUtils.getBean(TaskInfoService.class);
        taskDetailInfoService = (TaskDetailInfoService) ContextUtils.getBean(TaskDetailInfoService.class);
    }

    // for 框架使用者 ------------------------------------------------------------------ start
    /**
     * 处理目标数据。
     * @param now 执行时间
     */
    protected abstract boolean doExecute(Object id, Date now);

    /**
     * 获取待处理目标数据的数量。
     * 非补偿时使用。
     * @param now 执行时间
     * @see TraceJob#countDataTargetsForAll
     */
    protected abstract int countDataTargets(Date now);

    /**
     * 获取待处理目标数据的数量。
     * 补偿时使用。
     * @param now 执行时间
     * @see TraceJob#countDataTargetsForAll
     */
    protected int countDataTargetsForCompensate(Date now) {
        return taskDetailInfoService.countQuery(getDetailToHandleQueryParams());
    }

    /**
     * 获取待处理目标数据。
     * 非补偿时使用。
     * @param start 起始index
     * @param limit 获取数量
     * @param now 执行时间
     * @see TraceJob#getDataTargetIdsForAll
     */
    protected abstract List<?> getDataTargetIds(int start, int limit, Date now);

    /**
     * 获取待处理目标数据。
     * 补偿时使用。
     *
     * @param start 起始index
     * @param limit 获取数量
     * @param now 执行时间
     * @see TraceJob#getDataTargetIdsForAll
     */
    protected List<?> getDataTargetIdsForCompensate(int start, int limit, Date now) {
        return taskDetailInfoService.query(getDetailToHandleQueryParams(), null, start, limit);
    }

    /**
     * 执行任务前钩子，特殊需求可重写。
     */
    protected void setUp() {
        // do nothing
    }

    /**
     * 执行任务后钩子，特殊需求可重写。
     */
    protected void tearDown() {
        // do nothing
    }
    // for 框架使用者 ------------------------------------------------------------------ end

    /**
     * 请勿重写此方法！
     */
    @Override
    public void execute() {
        if (taskInfo == null) {
            taskInfo = new TaskInfo();
            taskInfo.setJobName(jobName);
            taskInfo.setJobClassName(getClass().getName());
            taskInfo.setMainTableName(mainTableName);
            taskInfo.setJobData(jobData.toJSONString());
            taskInfo.setAllowRetry(allowRetry);
            taskInfoService.insert(taskInfo);
        }

        try {
            dataCount = countDataTargetsForAll(taskInfo.getCreateTime());
            if (!isCompensate) {
                taskInfoService.resetStatics(taskInfo, dataCount);
            }

            setUp();
            doExecute(taskInfo.getCreateTime());
            taskInfo.checkIsFailed();
            taskInfoService.update(taskInfo);
        } catch (Exception e) {
            taskInfo.setIsFailed(true);
            taskInfoService.update(taskInfo);
            logger.error("Execute "+ getClass().getName() + " ERROR", e);
        } finally {
            tearDown();
        }
    }

    /**
     * 仅推荐装饰者继承重写。
     */
    protected void doExecute(Date now) {
        for (int start = 0 ; start < dataCount; start = start + limit) {
            try {
                doExecute(start, limit, now);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 请勿重写此方法！
     */
    protected void doExecute(int start, int limit, Date now) throws InvalidIdException {
        List<?> dataTargetIds = getDataTargetIdsForAll(start, limit, now);
        for (Object dataTargetId : dataTargetIds) {
            if (dataTargetId instanceof TaskDetailInfo) {
                dataTargetId = ((TaskDetailInfo) dataTargetId).getDataTargetId();
            }
            boolean success = false;
            try {
                success = doExecute(dataTargetId, now);
            } catch (Exception e) {
                logger.error("Execute date error ---dataTargetId:"+dataTargetId, e);
            }
            try {
                taskInfoService.updateStatics(taskInfo, dataTargetId, success);
            } catch (Exception e) {
                logger.error("Update updateStatics error--taskInfoId:"+taskInfo.getId()
                        +"----dataTargetId:"+dataTargetId+"----success:"+success
                        ,e);
            }
        }
    }

    /**
     * 检查id是否合法，允许id为TaskDetailInfo。
     */
    private void checkId(Object dataTargetId) throws InvalidIdException {
        if (dataTargetId instanceof TaskDetailInfo) {
            return;
        }
        if (dataTargetId instanceof Integer) {
            return;
        }
        if (dataTargetId instanceof String) {
            return;
        }
        if (dataTargetId instanceof Long) {
            return;
        }
        throw new InvalidIdException();
    }

    private int countDataTargetsForAll(Date now) {
        if (!isCompensate) {
            return countDataTargets(now);
        }
        return countDataTargetsForCompensate(now);
    }

    private List<?> getDataTargetIdsForAll(int start, int limit, Date now) throws InvalidIdException {
        List<?> dataTargetIds = null;
        if (isCompensate) {
            dataTargetIds = getDataTargetIdsForCompensate(start, limit, now);
        } else {
            dataTargetIds = getDataTargetIds(start, limit, now);
        }
        if (dataTargetIds.size() > 0) {
            checkId(dataTargetIds.get(0));
        }
        if (dataTargetIds == null) {
            return new ArrayList<>();
        }
        return dataTargetIds;
    }

    private List<CustomQueryParam> getDetailToHandleQueryParams() {
        Assert.notNull(taskInfo);
        QueryParamBuilder queryParamBuilder = QueryParamBuilder.newBuilder();
        queryParamBuilder.addWithValueQueryParam("taskInfoId", "=", taskInfo.getId());
        queryParamBuilder.addWithValueQueryParam("handleStatus", "<>", TaskDetailInfo.HandleStatus.SUCCESS);
        return queryParamBuilder.build();
    }

}
