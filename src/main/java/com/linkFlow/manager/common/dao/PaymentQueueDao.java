package com.linkFlow.manager.common.dao;

import com.linkFlow.manager.common.model.vo.PaymentQueueVO;
import com.linkFlow.manager.common.model.vo.PaymentQueueVOForSum;
import com.test32.common.generic.GenericDaoForBigInt;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface PaymentQueueDao extends GenericDaoForBigInt<PaymentQueueVO>
{
    boolean insertNotExist(PaymentQueueVO entity);

    boolean copyToCanceled(PaymentQueueVO entity);

    boolean increaseErrorCount(Map<String, Object> parameter);

    boolean updateWithPriorState(Map<String, Object> parameter);

    boolean resetTransferErrorWithPriorState(Map<String, Object> parameter);


    PaymentQueueVOForSum selectSumBySearch(Map<String, Object> parameter);
}