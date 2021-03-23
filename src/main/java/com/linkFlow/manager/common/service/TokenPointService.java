package com.linkFlow.manager.common.service;

import com.linkFlow.manager.common.model.vo.*;
import com.test32.common.generic.GenericServiceForBigInt;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface TokenPointService extends GenericServiceForBigInt<TokenPointVO>
{
    Long countBySearch(Map<String, Object> parameter);
    boolean createNew(MemberVO memberVO, TokenInfoVO tokenInfoVO);
    boolean isEnoughAmount(Long mbIdx, Integer tkIdx, BigDecimal reqAmount);

    TokenPointVO selectByMemberTokenIdxOrCreate(Long mbIdx, Integer tokenIdx, boolean isCreateIfNotExist);
    TokenPointVO selectByMemberTokenIdx(Long mbIdx, Integer tokenIdx);
    TokenPointVO selectByMemberTokenSymbol(Long mbIdx, String symbol);
    List<TokenPointVO> selectByMember(Long mbIdx);

    boolean addPoint(TokenPointVO target, BigDecimal amount);
    boolean subtractPoint(TokenPointVO target, BigDecimal amount);
    boolean subtractPointForced(TokenPointVO target, BigDecimal amount);

    boolean overwriteLockedPoint(TokenPointVO target, BigDecimal amount);

    List<TokenPointVOForApi> selectBySearchForApi(Map<String, Object> parameter);
    TokenPointVOForSum selectBySearchForSum(Map<String, Object> parameter);
    List<TokenPointVOForSum> selectBySearchListForSum(Map<String, Object> parameter);

}