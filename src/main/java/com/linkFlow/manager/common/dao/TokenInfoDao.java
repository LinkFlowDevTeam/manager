package com.linkFlow.manager.common.dao;

import com.linkFlow.manager.common.model.vo.TokenChartVOForApi;
import com.linkFlow.manager.common.model.vo.TokenInfoVO;
import com.linkFlow.manager.common.model.vo.TokenInfoVOForApi;
import com.linkFlow.manager.common.model.vo.TokenPriceVOForApi;
import com.test32.common.generic.GenericDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TokenInfoDao extends GenericDao<TokenInfoVO>
{
    List<TokenInfoVOForApi> selectBySearchForInfoApi(Map<String, Object> parameter);
    List<TokenPriceVOForApi> selectBySearchForPriceApi(Map<String, Object> parameter);
    List<TokenChartVOForApi> selectBySearchForChartApi(Map<String, Object> parameter);
}