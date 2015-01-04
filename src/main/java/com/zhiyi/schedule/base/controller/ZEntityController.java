package com.zhiyi.schedule.base.controller;

import chanedi.action.EntityController;
import chanedi.dao.complexQuery.Sort;
import chanedi.util.StringUtils;
import com.zhiyi.schedule.base.view.JDataTableView;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chanedi on 2014/12/25.
 */
public abstract class ZEntityController extends EntityController {

    @Override
    public Class<?> getTableViewClass() {
        return JDataTableView.class;
    }

    protected abstract String getTitle();

    @Override
    public String index(HttpServletRequest request, HttpServletResponse response, Model model) {
        model.addAttribute("title", getTitle());
        return super.index(request, response, model);
    }

    @Override
    protected Integer parseLimit(HttpServletRequest request) {
        String limit = request.getParameter("length");
        if (StringUtils.isEmpty(limit)) {
            return null;
        }
        return Integer.valueOf(limit);
    }

    @Override
    protected List<Sort> parseSort(HttpServletRequest request) {
        List<Sort> sorts = new ArrayList<>();
        for (int i = 0; ; i++) {
            String prefix = "order[" + i + "]";
            String columnIndex = request.getParameter(prefix + "[column]");
            if (columnIndex == null) {
                break;
            }
            Sort sort = new Sort();
            String column = request.getParameter("columns[" + columnIndex + "][data]");
            sort.setProperty(column);
            String dir = request.getParameter(prefix + "[dir]");
            if (dir.equals("asc")) {
                sort.setDirection(Sort.Direction.ASC);
            } else {
                sort.setDirection(Sort.Direction.DESC);
            }
            sorts.add(sort);
        }
        return sorts;
    }
}
