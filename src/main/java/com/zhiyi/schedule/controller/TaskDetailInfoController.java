package com.zhiyi.schedule.controller;

import chanedi.service.EntityService;
import chanedi.util.JSONUtils;
import com.zhiyi.schedule.base.controller.ZEntityController;
import com.zhiyi.schedule.model.TaskDetailInfo;
import com.zhiyi.schedule.model.TaskInfo;
import com.zhiyi.schedule.service.TaskDetailInfoService;
import com.zhiyi.schedule.service.TaskInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by chanedi on 2014/12/22.
 */
@Controller
@RequestMapping("/" + TaskDetailInfoController.NAME)
public class TaskDetailInfoController extends ZEntityController {

    public static final String NAME = "taskDetailInfo";

    @Resource
    private TaskDetailInfoService taskDetailInfoService;
    @Resource
    private TaskInfoService taskInfoService;

    @Override
    public String getIndexTilesName() {
        return NAME;
    }

    @Override
    public EntityService getEntityService() {
        return taskDetailInfoService;
    }

    @Override
    public Class<?> getEntityClass() {
        return TaskDetailInfo.class;
    }

    @Override
    protected String getTitle() {
        return "任务详细信息";
    }

    @Override
    public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
        Object taskInfoId = request.getParameter("taskInfoId");
        TaskInfo taskInfo = taskInfoService.getById(taskInfoId);
        model.addAttribute(taskInfo);
        model.addAttribute("handleStatus", JSONUtils.enumToJSONArray(TaskDetailInfo.HandleStatus.class));
        return super.index(request, response, model);
    }

}
