package com.zhiyi.schedule.test;

import com.zhiyi.schedule.TraceJob;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by chanedi on 2014/12/15.
 */
public class RandomFailedJob extends TraceJob {

    @Override
    protected boolean doExecute(Object id, Date now) {
        int success = ((int) (Math.random() * 10)) % 2;
        return success == 1;
    }

    @Override
    protected int countDataTargets(Date now) {
        return 7;
    }

    @Override
    protected List<?> getDataTargetIds(int start, int limit, Date now) {
        List<Integer> list = new ArrayList();
        list.add(23);
        list.add(213);
        list.add(223);
        list.add(323);
        list.add(253);
        list.add(243);
        list.add(263);
        return list;
    }
    
}