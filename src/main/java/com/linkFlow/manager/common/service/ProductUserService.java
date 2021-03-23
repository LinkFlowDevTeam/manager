package com.linkFlow.manager.common.service;

import com.linkFlow.manager.common.model.vo.ProductInfoVO;
import com.linkFlow.manager.common.model.vo.ProductUserVO;
import com.linkFlow.manager.common.model.vo.ProductUserVOForApi;
import com.linkFlow.manager.common.model.vo.ProductUserVOForSum;
import com.test32.common.generic.GenericService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ProductUserService extends GenericService<ProductUserVO>
{
    int STATE_NONE = 0;
    int STATE_ACTIVE = 1;
    int STATE_EXPIRED = 2;

    Integer countBySearch(Map<String, Object> parameter);
    List<ProductUserVOForApi> selectBySearchForApi(Map<String, Object> parameter);



    ProductUserVO selectByMemberProduct(long mbIdx, int pdIdx);
    boolean appendBaseAmount(ProductUserVO entity, BigDecimal amount);
    boolean appendInterestAmount(ProductUserVO entity, BigDecimal amount);

    ProductUserVOForSum selectSumDepositAmount(Long mbIdx, Integer pdIdx);

    boolean startProduct(ProductInfoVO productInfoVO);
    boolean applyDailyInterest(ProductInfoVO productInfoVO, ProductUserVO productUserVO, BigDecimal dailyInterest, int interestIndex);
}