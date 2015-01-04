package com.zhiyi.schedule.model;

import com.zhiyi.schedule.base.model.EntityWithTime;
import lombok.Data;

import javax.persistence.Table;

/**
 * Created by chanedi on 2014/12/17.
 */
@Data
@Table(name = "TB_SCHE_TASK_DETAIL_INFO")
public class TaskDetailInfo extends EntityWithTime {

    private Object id;
    private Object taskInfoId;
    private Object dataTargetId;
    private HandleStatus handleStatus;

    public static enum HandleStatus {
        TO_HANDLE("待处理"), SUCCESS("处理成功"), FAILED("处理失败");

        private String type;

        private HandleStatus(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return type;
        }
    }

}