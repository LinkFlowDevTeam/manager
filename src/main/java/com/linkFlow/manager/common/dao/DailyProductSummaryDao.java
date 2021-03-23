package com.linkFlow.manager.common.dao;

import com.linkFlow.manager.common.model.vo.DailyProductSummaryVO;
import com.linkFlow.manager.common.model.vo.DailyProductSummaryVOForApi;
import com.test32.common.generic.GenericDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DailyProductSummaryDao extends GenericDao<DailyProductSummaryVO>
{
    boolean updateByKey(DailyProductSummaryVO entity);
    List<DailyProductSummaryVOForApi> selectBySearchForApi(Map<String, Object> searchMap);
}