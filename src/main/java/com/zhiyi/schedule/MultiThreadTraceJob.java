package com.zhiyi.schedule;

import com.zhiyi.schedule.exception.InvalidIdException;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by chanedi on 2014/12/17.
 */
public class MultiThreadTraceJob extends TraceJob {

    private Executor executor;
    @Setter
    private int threadCount = 10;
    @Setter
    private long timeoutMinutes = 60;
    @Setter
    private TraceJob traceJob;

    @Override
    protected boolean doExecute(Object id, Date now) {
        return traceJob.doExecute(id, now);
    }

    @Override
    protected int countDataTargets(Date now) {
        return traceJob.countDataTargets(now);
    }

    @Override
    protected List<?> getDataTargetIds(int start, int limit, Date now) {
        return traceJob.getDataTargetIds(start, limit, now);
    }

    @Override
    protected void setUp() {
        traceJob.setUp();
        executor = Executors.newFixedThreadPool(threadCount);
    }

    @Override
    protected void tearDown() {
        traceJob.tearDown();
    }

    @Override
    protected void doExecute(final Date now) {
        final TraceJob traceJob = this.traceJob;
        int count = dataCount / limit + 1;
        final CountDownLatch countDownLatch = new CountDownLatch(count);
        for (int start = 0 ; start < dataCount; start = start + limit) {
            final int _start = start;
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        traceJob.doExecute(_start, limit, now);
                    } catch (InvalidIdException e) {
                        logger.error(e.getMessage(), e);
                    } finally {
                        countDownLatch.countDown();
                    }
                }
            });
        }
        try {
            countDownLatch.await(timeoutMinutes, TimeUnit.MINUTES);
            if (countDownLatch.getCount() > 0) {
                logger.warn("Job run time out: " + timeoutMinutes + "minutes.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }

}
