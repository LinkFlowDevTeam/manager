package com.linkFlow.manager.common.dao;

import com.linkFlow.manager.common.model.vo.AccessIpVO;
import com.test32.common.generic.GenericDaoForBigInt;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface AccessIpDao extends GenericDaoForBigInt<AccessIpVO>
{
    boolean insertNotExist(AccessIpVO entity);
    AccessIpVO selectByIp(String ip);
    boolean increaseErrorCount(Map<String, Object> map);
    boolean decreaseErrorCount(Map<String, Object> map);
    boolean resetErrorCount(Map<String, Object> map);
}
