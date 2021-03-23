package com.linkFlow.manager.common.dao;

import com.linkFlow.manager.common.model.vo.MemberVO;
import com.linkFlow.manager.common.model.vo.MemberVOForApi;
import com.test32.common.generic.GenericDaoForBigInt;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MemberDao extends GenericDaoForBigInt<MemberVO>
{
    boolean increaseErrorCount(Map<String, Object> searchMap);
    boolean decreaseErrorCount(Map<String, Object> searchMap);
    boolean resetErrorCount(Map<String, Object> searchMap);

    List<MemberVOForApi> selectBySearchForApi(Map<String, Object> searchMap);
}