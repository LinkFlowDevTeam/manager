package com.linkFlow.manager.common.service;

import com.linkFlow.manager.common.model.vo.MemberTierVO;
import com.linkFlow.manager.common.model.vo.MemberTierVOForApi;
import com.test32.common.generic.GenericService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface MemberTierService extends GenericService<MemberTierVO>
{
    MemberTierVO selectByRate(BigDecimal rate);
    MemberTierVO selectByAmount(BigDecimal amount);
    MemberTierVO selectByTier(int tier);
    List<MemberTierVOForApi> selectBySearchForApi(Map<String, Object> parameter);
}