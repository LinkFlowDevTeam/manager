package com.linkFlow.manager.common.dao;

import com.linkFlow.manager.common.model.vo.ApiCurrencyVO;
import com.test32.common.generic.GenericDaoForBigInt;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ApiCurrencyDao extends GenericDaoForBigInt<ApiCurrencyVO>
{
    boolean insertOrUpdate(ApiCurrencyVO entity);
}