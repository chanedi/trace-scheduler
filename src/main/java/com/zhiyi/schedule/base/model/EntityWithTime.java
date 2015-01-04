package com.zhiyi.schedule.base.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;

/**
 * Created by chanedi on 2014/12/26.
 */
@Data
public abstract class EntityWithTime extends chanedi.model.EntityWithTime {

    @Override
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    public Date getCreateTime() {
        return super.getCreateTime();
    }

    @Override
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    public Date getModifyTime() {
        return super.getModifyTime();
    }

}
