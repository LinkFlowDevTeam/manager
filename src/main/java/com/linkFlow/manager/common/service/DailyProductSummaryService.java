package com.linkFlow.manager.common.service;


import com.linkFlow.manager.common.model.vo.DailyProductSummaryVO;
import com.linkFlow.manager.common.model.vo.DailyProductSummaryVOForApi;
import com.test32.common.generic.GenericService;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface DailyProductSummaryService extends GenericService<DailyProductSummaryVO>
{
    boolean insertOrUpdate(DailyProductSummaryVO entity);

    void runYesterday();
    void runDaily(Date targetDate, boolean isFixed);

    List<DailyProductSummaryVOForApi> selectBySearchForApi(Map<String, Object> parameter);
}