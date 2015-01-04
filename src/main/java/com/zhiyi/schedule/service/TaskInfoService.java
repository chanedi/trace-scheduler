package com.zhiyi.schedule.service;

import chanedi.service.EntityService;
import com.zhiyi.schedule.model.TaskInfo;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by chanedi on 2014/12/18.
 */
public interface TaskInfoService extends EntityService<TaskInfo> {

    /**
     * 执行补偿
     * @param id
     */
    public void compensate(String id);

    /**
     * 重新执行任务
     * @param id
     */
    public void retry(String id);

    /**
     * 更新统计信息。
     * @param taskInfo
     * @param dataTargetId
     * @param success 是否执行成功
     */
    @Transactional
    public void updateStatics(TaskInfo taskInfo, Object dataTargetId, boolean success);

    /**
     * 重置统计信息，清空{@link com.zhiyi.schedule.model.TaskDetailInfo}
     * @param taskInfo
     * @param dataCount 重置后的待处理数据数量
     */
    @Transactional
    public void resetStatics(TaskInfo taskInfo, int dataCount);
}
