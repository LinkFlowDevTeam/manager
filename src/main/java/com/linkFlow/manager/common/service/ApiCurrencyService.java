package com.linkFlow.manager.common.service;

import com.linkFlow.manager.common.model.vo.ApiCurrencyVO;
import com.test32.common.generic.GenericDaoForBigInt;
import com.test32.common.paging.PagingData;

import java.util.Map;

public interface ApiCurrencyService extends GenericDaoForBigInt<ApiCurrencyVO>
{
    PagingData selectPagingBySearch(Map<String, Object> parameter);
    boolean insertOrUpdate(ApiCurrencyVO entity);
    ApiCurrencyVO selectByCurrency(String currency);
}