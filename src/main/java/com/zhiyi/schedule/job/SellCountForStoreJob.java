package com.zhiyi.schedule.job;

import chanedi.context.ContextUtils;
import com.zhiyi.deliveryinterface.dto.SellCountDto;
import com.zhiyi.deliveryinterface.interfaces.SellCountInterface;
import com.zhiyi.schedule.TraceJob;

import java.util.Date;
import java.util.List;


public class SellCountForStoreJob extends TraceJob{


    private SellCountInterface calSellCountProxy=(SellCountInterface) ContextUtils.getBean(SellCountInterface.class);

    @Override
    protected boolean doExecute(Object id, Date now) {
        return calSellCountProxy.handleCalSell(id, SellCountDto.SellCountType.STORE,true);
    }

    @Override
    protected int countDataTargets(Date now) {
        return calSellCountProxy.getCalSellCountTotal(SellCountDto.SellCountType.STORE, true);
    }

    @Override
    protected List<?> getDataTargetIds(int start, int limit, Date now) {
        List<String> ids=(List<String>)calSellCountProxy.getCalSellDataTargetIds(SellCountDto.SellCountType.STORE,start,limit,true);
        return ids;
    }
}
