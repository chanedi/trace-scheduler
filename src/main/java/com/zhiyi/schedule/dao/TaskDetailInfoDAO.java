package com.zhiyi.schedule.dao;

import chanedi.dao.EntityDAO;
import com.zhiyi.schedule.model.TaskDetailInfo;

/**
 * Created by chanedi on 2014/12/19.
 */
public interface TaskDetailInfoDAO extends EntityDAO<TaskDetailInfo> {

    public void deleteByTaskInfoId(Object id);

}
