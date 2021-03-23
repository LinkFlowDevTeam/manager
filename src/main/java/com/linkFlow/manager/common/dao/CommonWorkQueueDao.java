package com.linkFlow.manager.common.dao;

import com.linkFlow.manager.common.model.vo.CommonWorkQueueVO;
import com.test32.common.generic.GenericDaoForBigInt;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommonWorkQueueDao extends GenericDaoForBigInt<CommonWorkQueueVO>
{
    boolean updateStartDate(Long idx);
}