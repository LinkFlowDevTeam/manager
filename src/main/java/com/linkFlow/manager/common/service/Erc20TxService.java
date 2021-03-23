package com.linkFlow.manager.common.service;


import com.linkFlow.manager.common.model.vo.Erc20TxVO;
import com.test32.common.generic.GenericServiceForBigInt;

public interface Erc20TxService extends GenericServiceForBigInt<Erc20TxVO>
{
    int STATE_NONE = 0;
    int STATE_USELESS = 1;
    int STATE_TRANSFER_REQUEST = 2;
    int STATE_TRANSFER_APPLIED = 3;
    int STATE_TRANSFER_USER_NOT_FOUND = 4;

    int STATE_METHOD_CHECK_REQUIRED = 5;

    int STATE_MANAGER_SEND_ETH = 6;

    String STATUS_RECEIPT_SUCCESS = "0x1";
    String STATUS_RECEIPT_FAILURE = "0x0";



    int TX_STATE_NONE = 0;
    int TX_STATE_PENDING = 1;
    int TX_STATE_MISSING = 2;
    int TX_STATE_CONFIRMED = 3;


    int RS_STATE_CONFIRMED = 1;
    int RS_STATE_REJECTED = 2;


    int FLAG_MANAGER_TRUE = 1;
    int FLAG_MANAGER_FALSE = 2;

    boolean insertNotExist(Erc20TxVO erc20TxVO);
    long countByBlockNumber(long blockNumber);

    boolean deleteUseless(int searchSecond);
}