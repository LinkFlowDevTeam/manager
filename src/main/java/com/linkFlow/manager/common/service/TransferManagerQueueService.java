package com.linkFlow.manager.common.service;

import com.linkFlow.manager.common.model.BaseResponse;
import com.linkFlow.manager.common.model.vo.TransferManagerQueueVO;
import com.test32.common.generic.GenericService;

import java.util.List;

public interface TransferManagerQueueService extends GenericService<TransferManagerQueueVO>
{
    int STATE_DISABLE = 0;
    int STATE_ENABLE = 1;
    int STATE_GAS_FEE_ONLY = 2;


    int TARGET_STATE_READY = 0;
    int TARGET_STATE_WORKING = 1;
    int TARGET_STATE_ERROR = 2;


    boolean isAddressExist(String address);
    TransferManagerQueueVO selectByAddress(String address, String symbol);


    List<TransferManagerQueueVO> selectEnabledByToken(Integer tkIdx);
    List<TransferManagerQueueVO> selectEnabledBySymbol(String tkSymbol);

    List<TransferManagerQueueVO> selectForGasFeeOnly();

    //List<TransferManagerQueueVO> selectEnabled();
//    List<TransferManagerQueueVO> selectEnabledByTargetIdx(Integer targetIdx);

    Integer countDistinctWorker();

    boolean notifyTargetDone(Long targetIdx);

    void checkPaymentQueueState();

    BaseResponse refreshErc20Point(Integer targetIdx);
    BaseResponse toggleWorker(Integer targetIdx);
}