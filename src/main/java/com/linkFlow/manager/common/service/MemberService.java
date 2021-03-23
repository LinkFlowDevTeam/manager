package com.linkFlow.manager.common.service;

import com.linkFlow.manager.common.model.vo.MemberVO;
import com.linkFlow.manager.common.model.vo.MemberVOForApi;
import com.test32.common.generic.GenericServiceForBigInt;

import java.util.Date;
import java.util.Map;

public interface MemberService extends GenericServiceForBigInt<MemberVO>
{
    boolean increaseErrorCount(MemberVO memberVO);
    boolean decreaseErrorCount(MemberVO memberVO);
    boolean resetErrorCount(MemberVO memberVO);

    void refreshTierAll();
    boolean refreshLfTokenLocked(long mbIdx);

    long countNewUser(Date targetDate);
    long countBySearch(Map<String, Object> parameter);

    MemberVOForApi selectForApi(long idx);
    MemberVOForApi selectBySearchForApi(Map<String, Object> searchMap);

    MemberVO selectById(String id);
    //MemberVO selectByErc20Address(String erc20Address);
}