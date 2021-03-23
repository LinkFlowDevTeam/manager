package com.linkFlow.manager.common.dao;

import com.linkFlow.manager.common.model.vo.CommonTicketVO;
import com.test32.common.generic.GenericDaoForBigInt;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface CommonTicketDao extends GenericDaoForBigInt<CommonTicketVO>
{
    boolean updateSign(Map<String, Object> parameter);
    boolean updateDate(Map<String, Object> parameter);
    boolean increaseErrorCount(Map<String, Object> parameter);
    boolean deleteByUpdateDate(Map<String, Object> parameter);
    boolean deleteOverSecond(Map<String, Object> parameter);
}