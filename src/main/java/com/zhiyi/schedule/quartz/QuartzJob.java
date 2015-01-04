package com.zhiyi.schedule.quartz;

import com.alibaba.fastjson.JSONObject;
import com.zhiyi.schedule.Job;
import com.zhiyi.schedule.MultiThreadTraceJob;
import com.zhiyi.schedule.TraceJob;
import org.quartz.*;
import org.quartz.impl.JobDetailImpl;

/**
 * 适配器。
 * Created by chanedi on 2014/12/17.
 */
public class QuartzJob implements org.quartz.Job {

    public static final String JOB_CLASS = "jobClass";
    public static final String MAIN_TABLE_NAME = "mainTableName";
    public static final String THREAD_COUNT = "threadCount";
    public static final String TIMEOUT = "timeoutMinutesForMultiThread";
    public static final String ALLOW_RETRY = "allowRetry";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getMergedJobDataMap();
        Class<? extends Job> jobClass = (Class<? extends Job>) jobDataMap.get(JOB_CLASS);
        String mainTableName = (String) jobDataMap.get(MAIN_TABLE_NAME);
        int threadCount = (Integer) jobDataMap.get(THREAD_COUNT);
        long timeout = (Long) jobDataMap.get(TIMEOUT);
        boolean allowRetry = (Boolean) jobDataMap.get(ALLOW_RETRY);

        JobDetailImpl jobDetail = (JobDetailImpl) context.getJobDetail();

        try {
            Job job = jobClass.newInstance();
            if (job instanceof TraceJob) {
                TraceJob traceJob = (TraceJob) job;
                traceJob.setMainTableName(mainTableName);
                traceJob.setAllowRetry(allowRetry);
                traceJob.setJobName(jobDetail.getName());
                JSONObject jobData = parseJobData(context);
                traceJob.setJobData(jobData);
            }
            if (threadCount > 1 && job instanceof MultiThreadTraceJob) {
                MultiThreadTraceJob multiThreadTraceJob = new MultiThreadTraceJob();
                multiThreadTraceJob.setThreadCount(threadCount);
                multiThreadTraceJob.setTimeoutMinutes(timeout);
            }
            job.execute();
        } catch (Exception e) {
            e.printStackTrace();
            throw new JobExecutionException(e.getMessage(), e);
        }
    }

    private JSONObject parseJobData(JobExecutionContext context) {
        JSONObject jobData = new JSONObject();
        Trigger trigger = context.getTrigger();

        JSONObject triggerInfo = new JSONObject();
        if (trigger instanceof SimpleTrigger) {
            triggerInfo.put("type", "simple");
            SimpleTrigger simpleTrigger = (SimpleTrigger) trigger;
            triggerInfo.put("repeatCount", simpleTrigger.getRepeatCount());
            triggerInfo.put("repeatInterval", simpleTrigger.getRepeatInterval());
        } else if (trigger instanceof CronTrigger) {
            triggerInfo.put("type", "cron");
            CronTrigger cronTrigger = (CronTrigger) trigger;
            triggerInfo.put("cronExpression", cronTrigger.getCronExpression());
        }

        jobData.put("trigger", triggerInfo);
        return jobData;
    }

}
