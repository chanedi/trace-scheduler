package com.zhiyi.schedule.controller;

import chanedi.dao.complexQuery.QueryParamBuilder;
import chanedi.service.EntityService;
import com.zhiyi.schedule.base.controller.ZEntityController;
import com.zhiyi.schedule.model.TaskInfo;
import com.zhiyi.schedule.service.TaskInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by chanedi on 2014/12/22.
 */
@Controller
@RequestMapping("/" + TaskInfoController.NAME)
public class TaskInfoController extends ZEntityController {

    public static final String NAME = "taskInfo";

    @Resource
    private TaskInfoService taskInfoService;

    @Override
    public String getIndexTilesName() {
        return NAME;
    }

    @Override
    public EntityService getEntityService() {
        return taskInfoService;
    }

    @Override
    public Class<?> getEntityClass() {
        return TaskInfo.class;
    }

    @Override
    protected String getTitle() {
        return "任务列表";
    }

    @ResponseBody
    @RequestMapping(value = "/compensate/{id}")
    public boolean compensate(@PathVariable String id) {
        taskInfoService.compensate(id);
        return true;
    }

    @ResponseBody
    @RequestMapping(value = "/retry/{id}")
    public boolean retry(@PathVariable String id) {
        taskInfoService.retry(id);
        return true;
    }

    @Override
    protected void buildCustomQueryParam(QueryParamBuilder builder, HttpServletRequest request, String prefix) {
        String field = request.getParameter(prefix + getQueryFilterFieldName());
        Object value = request.getParameter(prefix + getQueryFilterValueName());
        if (field.equals("onlyFailure") && new Boolean(value.toString())) {
            builder.addWithValueQueryParam("isFailed", "=", true);
        }
    }

}
