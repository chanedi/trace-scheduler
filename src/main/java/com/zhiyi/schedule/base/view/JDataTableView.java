package com.zhiyi.schedule.base.view;

import chanedi.action.view.DefaultTableView;

import java.util.List;

/**
 * Created by chanedi on 2014/12/25.
 * jQuery的ui插件datatable
 */
public class JDataTableView extends DefaultTableView {

    public int getRecordsTotal() {
        return getTotal();
    }

    public int getRecordsFiltered() {
        return getTotal();
    }

    public List<?> getData() {
        return getRows();
    }

}
