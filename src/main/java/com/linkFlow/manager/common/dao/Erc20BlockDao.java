package com.linkFlow.manager.common.dao;

import com.linkFlow.manager.common.model.vo.Erc20BlockVO;
import com.test32.common.generic.GenericDaoForBigInt;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface Erc20BlockDao extends GenericDaoForBigInt<Erc20BlockVO>
{
    boolean insertNotExist(Erc20BlockVO entity);
    boolean updateByBlockNumber(Erc20BlockVO erc20BlockVO);
}