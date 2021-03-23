package com.linkFlow.manager.common.service;

import com.linkFlow.manager.common.model.BaseResponse;
import com.linkFlow.manager.common.model.vo.ProductInfoVO;
import com.linkFlow.manager.common.model.vo.ProductInfoVOForApi;
import com.test32.common.generic.GenericService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ProductInfoService extends GenericService<ProductInfoVO>
{
    int STATE_HIDE = 0;
    int STATE_ON_SALE = 1;
    int STATE_SOLDOUT = 2;
    int STATE_PREPARE = 3;
    int STATE_STARTED = 4;
    int STATE_EXPIRED = 5;


    ProductInfoVO selectByErc20Address(String address);

    List<ProductInfoVOForApi> selectBySearchForApi(Map<String, Object> parameter);
    Integer countBySearch(Map<String, Object> parameter);

    BaseResponse refreshErc20Point(Integer pdIdx);

    BaseResponse start(Integer pdIdx);

    boolean refreshBaseAmount(int pdIdx);

    boolean appendInterestAmount(int pdIdx, BigDecimal amount);

    boolean runDefaultInterest(ProductInfoVO productInfoVO);
    //boolean runDailyProductPurchase(ProductInfoVO productInfoVO);
}