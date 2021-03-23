package com.linkFlow.manager.common.dao;

import com.linkFlow.manager.common.model.vo.TokenPointVO;
import com.linkFlow.manager.common.model.vo.TokenPointVOForApi;
import com.linkFlow.manager.common.model.vo.TokenPointVOForSum;
import com.test32.common.generic.GenericDaoForBigInt;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TokenPointDao extends GenericDaoForBigInt<TokenPointVO>
{
    boolean insertNotExist(TokenPointVO entity);

    boolean addPoint(Map<String, Object> searchMap);
    boolean subtractPoint(Map<String, Object> searchMap);
    boolean subtractPointForced(Map<String, Object> searchMap);

    boolean overwriteLockedPoint(Map<String, Object> searchMap);

    List<TokenPointVOForApi> selectBySearchForApi(Map<String, Object> parameter);

    TokenPointVOForSum selectBySearchForSum(Map<String, Object> parameter);

    List<TokenPointVOForSum> selectBySearchListForSum(Map<String, Object> parameter);
}