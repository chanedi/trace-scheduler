package com.zhiyi.schedule.service.impl;

import chanedi.service.EntityServiceImpl;
import com.zhiyi.schedule.Job;
import com.zhiyi.schedule.TraceJob;
import com.zhiyi.schedule.dao.TaskDetailInfoDAO;
import com.zhiyi.schedule.dao.TaskInfoDAO;
import com.zhiyi.schedule.exception.BaseException;
import com.zhiyi.schedule.model.TaskDetailInfo;
import com.zhiyi.schedule.model.TaskInfo;
import com.zhiyi.schedule.service.TaskInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.Executor;

/**
 * Created by chanedi on 2014/12/18.
 */
@Service
public class TaskInfoServiceImpl extends EntityServiceImpl<TaskInfo> implements TaskInfoService {

    @Resource
    private Executor taskExecutor;
    @Resource
    private TaskInfoDAO taskInfoDAO;
    @Resource
    private TaskDetailInfoDAO taskDetailInfoDAO;

    @Override
    public void compensate(String id) {
        execute(id, true);
    }

    @Override
    public void retry(String id) {
        execute(id, false);
    }

    private void execute(String id, boolean isCompensate) {
        TaskInfo taskInfo = taskInfoDAO.getById(id);
        try {
            Class<?> jobClass = Class.forName(taskInfo.getJobClassName());
            final Job instance = (Job) jobClass.newInstance();
            if (instance instanceof TraceJob) {
                ((TraceJob) instance).setCompensate(isCompensate);
                ((TraceJob) instance).setTaskInfo(taskInfo);
            }
            taskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    instance.execute();
                }
            });
        } catch (Exception e) {
            new BaseException(e);
        }
    }

    @Override
    public void updateStatics(TaskInfo taskInfo, Object dataTargetId, boolean success) {
        TaskDetailInfo params = new TaskDetailInfo();
        params.setDataTargetId(dataTargetId);
        params.setTaskInfoId(taskInfo.getId());
        TaskDetailInfo taskDetailInfo = taskDetailInfoDAO.getOne(params);
        if (taskDetailInfo == null) { // 非补偿任务
            taskInfo.addProcessedCount();

            taskDetailInfo = new TaskDetailInfo();
            taskDetailInfo.setTaskInfoId(taskInfo.getId());
            taskDetailInfo.setDataTargetId(dataTargetId);
            if (!success) {
                taskInfo.addFailureCount();
            }
        } else {
            if (success) {
                taskInfo.minusFailureCount();
            }
        }

        if (success) {
            taskDetailInfo.setHandleStatus(TaskDetailInfo.HandleStatus.SUCCESS);
        } else {
            taskDetailInfo.setHandleStatus(TaskDetailInfo.HandleStatus.FAILED);
        }

        taskInfoDAO.update(taskInfo);
        if (taskDetailInfo.getId() == null) {
            taskDetailInfoDAO.insert(taskDetailInfo);
        } else {
            taskDetailInfoDAO.update(taskDetailInfo);
        }
    }

    @Override
    public void resetStatics(TaskInfo taskInfo, int dataCount) {
        taskInfo.setIsFailed(false);
        taskInfo.setDataCount(dataCount);
        taskInfo.setFailureCount(0);
        taskInfo.setProcessedCount(0);
        taskInfoDAO.update(taskInfo);
        taskDetailInfoDAO.deleteByTaskInfoId(taskInfo.getId());
    }

}
