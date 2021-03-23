package com.linkFlow.manager.common.dao;

import com.linkFlow.manager.common.model.vo.TransferManagerQueueVO;
import com.test32.common.generic.GenericDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface TransferManagerQueueDao extends GenericDao<TransferManagerQueueVO>
{
    Integer countDistinctWorker(Map<String, Object> parameter);
}