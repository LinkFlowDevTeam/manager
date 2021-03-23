package com.linkFlow.manager.common.dao;

import com.linkFlow.manager.common.model.vo.Erc20TxVO;
import com.test32.common.generic.GenericDaoForBigInt;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface Erc20TxDao extends GenericDaoForBigInt<Erc20TxVO>
{
    boolean insertNotExist(Erc20TxVO entity);
    boolean deleteUseless(Map<String, Object> parameter);
}