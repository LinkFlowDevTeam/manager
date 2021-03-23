package com.linkFlow.manager.common.dao;

import com.linkFlow.manager.common.model.vo.TransactionVO;
import com.linkFlow.manager.common.model.vo.TransactionVOForApi;
import com.test32.common.generic.GenericDaoForBigInt;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TransactionDao extends GenericDaoForBigInt<TransactionVO>
{
    List<TransactionVOForApi> selectBySearchForApi(Map<String, Object> map);
}