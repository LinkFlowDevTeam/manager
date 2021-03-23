package com.linkFlow.manager.common.service;


import com.linkFlow.manager.common.model.vo.Erc20QueueVO;
import com.linkFlow.manager.common.model.vo.Erc20TxVO;
import com.test32.common.generic.GenericServiceForBigInt;
import com.test32.common.model.blockChain.erc20.jsonRpc.JsonRpcGetTransactionByHash;

public interface Erc20QueueService extends GenericServiceForBigInt<Erc20QueueVO>
{
    int STATE_NONE = 0;
    int STATE_TRANSFER_REQUEST = 1;
    int STATE_TRANSFER_RESPONSE_SUCCESS = 2;
    int STATE_TRANSFER_RESPONSE_ERROR = 3;

    int STATE_TRANSFER_REQUEST_ERROR = 4;



    int IO_DEPOSIT = 1;
    int IO_WITHDRAW = 2;



    int CHAIN_STATE_NONE = 0;
    int CHAIN_STATE_INSERTED = 1;

    // from receipt
    int CHAIN_STATE_RECEIPT_SUCCESS = 2;
    int CHAIN_STATE_RECEIPT_ERROR = 3;

    // from tx
    int CHAIN_STATE_TX_PENDING = 4;
    int CHAIN_STATE_TX_MISSING = 5;
    int CHAIN_STATE_TX_CONFIRMED = 6;

    // update by scheduler
    boolean notifyTxInserted(Erc20TxVO erc20TxVO);
    boolean notifyTxChecked(String txHash, JsonRpcGetTransactionByHash jsonRpcGetTransactionByHash);
    boolean notifyReceiptChecked(Long erc20TxVO_idx);
}