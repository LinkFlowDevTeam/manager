package com.linkFlow.manager.common.dao;

import com.linkFlow.manager.common.model.vo.CommonSnapshotVO;
import com.test32.common.generic.GenericDaoForBigInt;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommonSnapshotDao extends GenericDaoForBigInt<CommonSnapshotVO>
{
    boolean insertOrUpdate(CommonSnapshotVO entity);
    boolean updateByKey(CommonSnapshotVO entity);
}