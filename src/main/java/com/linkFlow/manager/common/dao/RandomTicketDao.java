package com.linkFlow.manager.common.dao;

import com.linkFlow.manager.common.model.vo.RandomTicketVO;
import com.test32.common.generic.GenericDaoForBigInt;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface RandomTicketDao extends GenericDaoForBigInt<RandomTicketVO>
{
    boolean deleteByUpdateDate(Map<String, Object> parameter);
    boolean deleteOverSecond(Map<String, Object> parameter);
    boolean deleteExpired(Map<String, Object> parameter);

    boolean deleteByKey(Map<String, Object> parameter);
    boolean deleteByValue(Map<String, Object> parameter);
}