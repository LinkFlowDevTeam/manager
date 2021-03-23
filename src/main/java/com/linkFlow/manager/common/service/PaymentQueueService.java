package com.linkFlow.manager.common.service;

import com.linkFlow.manager.common.model.BaseResponse;
import com.linkFlow.manager.common.model.vo.Erc20TxVO;
import com.linkFlow.manager.common.model.vo.MemberVO;
import com.linkFlow.manager.common.model.vo.PaymentQueueVO;
import com.linkFlow.manager.common.model.vo.TokenPointVO;
import com.test32.common.generic.GenericServiceForBigInt;
import com.test32.common.model.blockChain.erc20.jsonRpc.JsonRpcGetTransactionByHash;
import com.test32.common.model.blockChain.erc20.jsonRpc.JsonRpcGetTransactionReceipt;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

public interface PaymentQueueService extends GenericServiceForBigInt<PaymentQueueVO>
{
    int TRANSFER_TYPE_DEPOSIT = 1;
    int TRANSFER_TYPE_WITHDRAW = 2;
    int TRANSFER_TYPE_PRODUCT_PURCHASE = 3;
    int TRANSFER_TYPE_LF_DEPOSIT = 4;
    //int TRANSFER_TYPE_DATA_WITH_ETH = 5;

    //int TRANSFER_TYPE_WRAPPER_PRODUCT_PURCHASE_TRANSFER = 6;
    //int TRANSFER_TYPE_WRAPPER_INTEREST_TRANSFER = 7;
    //int TRANSFER_TYPE_WRAPPER_USER_INTEREST_LOG = 8;



    int TR_FLAG_REAL_TRANSFER = 1;
    //int TR_FLAG_INNER_TRANSFER = 2;


    int STATE_NONE = 0;
    int STATE_PROCESSED = 1;
    int STATE_ERROR = 2;
    int STATE_WITHDRAW_DENIED_REQUEST = 3;
    int STATE_WITHDRAW_DENIED_SUCCESS = 4;

    int STATE_TRANSFER_REQUESTED = 6;
    int STATE_TRANSFER_SUCCESS = 7;
    int STATE_TRANSFER_ERROR = 8;

    int STATE_WITHDRAW_SCHEDULED_INSERT = 14;

    int STATE_TRANSFER_TX_PENDING = 15;
    int STATE_TRANSFER_TX_MISSING = 16;
    int STATE_TRANSFER_TX_CONFIRMED = 17;
    int STATE_TRANSFER_RS_RECEIVED_CONFIRMED = 18;
    int STATE_TRANSFER_RS_RECEIVED_REJECTED = 19;


    Long countBySearch(Map<String, Object> parameter);
    boolean increaseErrorCount(Long pqIdx);
    boolean updateWithPriorState(Map<String, Object> parameter);

    boolean insertNotExist(PaymentQueueVO entity);
    boolean copyToCanceled(PaymentQueueVO entity);

    boolean insertWithdrawalRequest(MemberVO memberVO, TokenPointVO tokenPointVO, BigDecimal reqAmount, String uniqueId, String transferTo, String transferMemo);
    BaseResponse applyWithdraw(PaymentQueueVO entity, boolean isFromManager);
    BaseResponse applyWithdrawWithGasLimit(PaymentQueueVO entity, boolean isFromManager, BigInteger gasLimit, BigInteger gasPrice, Integer gasPriceMultiplier);
    BaseResponse denyWithdraw(PaymentQueueVO entity, boolean isFromManager);
    BaseResponse denyWithdrawWithMessage(PaymentQueueVO entity, String message);
    BaseResponse markAsWithdrawSuccess(PaymentQueueVO entity, boolean isFromManager);
    BaseResponse markAsWithdrawScheduled(PaymentQueueVO entity, boolean isFromManager);

    void denyWithdraw(PaymentQueueVO entity);

    //void applyExternalDeposit(PaymentQueueVO item);
    void applyProductPurchase(PaymentQueueVO item);
    void applyLfTokenDeposit(PaymentQueueVO item);

    //void notifyReceiptChecked(Erc20TxVO erc20TxVO);
    void notifyTxChecked(String txHash, JsonRpcGetTransactionByHash jsonRpcGetTransactionByHash);
    void notifyReceiptChecked(Erc20TxVO erc20TxVO, JsonRpcGetTransactionReceipt jsonRpcGetTransactionReceipt, boolean isForceRetry);

    BaseResponse resetTransferErrorState(PaymentQueueVO entity);


    PaymentQueueVO selectLastByType(int type);
    PaymentQueueVO selectByTypeUnique(int type, String uniqueId);


    //PaymentQueueVO generateWrapperTokenProductPurchase(ProductInfoVO productInfoVO, BigDecimal sumAmount, String yyyyMMdd);
    //PaymentQueueVO generateWrapperTokenInterestTransferRequest(ProductInfoVO productInfoVO, BigDecimal sumAmount, int interestIndex);
    //PaymentQueueVO generateWrapperTokenMemberLog(MemberVO memberVO, ProductUserVO productUserVO, BigDecimal sumAmount, int interestIndex);
}