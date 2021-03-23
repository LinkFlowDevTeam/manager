package com.linkFlow.manager.common.service;

import com.linkFlow.manager.common.model.vo.CommonSnapshotVO;
import com.linkFlow.manager.common.model.vo.MemberVO;
import com.test32.common.generic.GenericServiceForBigInt;

import java.math.BigDecimal;

public interface CommonSnapshotService extends GenericServiceForBigInt<CommonSnapshotVO>
{
    int TYPE_MEMBER = 1;
    boolean insertOrUpdate(CommonSnapshotVO entity);

    boolean isDataExist(int type, String searchStartDate);

    boolean isMemberDataExist(String searchStartDate);
    CommonSnapshotVO generateMember(MemberVO memberVO, BigDecimal baseAmount, Integer childCount, BigDecimal childBaseAmount);
}