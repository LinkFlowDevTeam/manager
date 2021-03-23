package com.linkFlow.manager.common.service;

import com.linkFlow.manager.common.model.vo.*;
import com.test32.common.generic.GenericServiceForBigInt;
import com.test32.common.paging.PagingData;

import java.math.BigDecimal;
import java.util.Map;

public interface TransactionService extends GenericServiceForBigInt<TransactionVO>
{
    Integer STATE_NONE = 0;
    Integer STATE_PROCESSED = 1;
    Integer STATE_ERROR = 2;
    Integer STATE_ROLLBACK = 3;


    int POINT_UPDATE_NONE = 0;      // 포인트 변동없음
    int POINT_UPDATE_IN = 1;        // 포인트 +
    int POINT_UPDATE_OUT = 2;       // 포인트 -
    int POINT_UPDATE_IO = 3;       // 포인트 +-



    int TYPE_DEPOSIT = 1;           // 거래소로부터 입금
    int TYPE_WITHDRAW_REQUEST = 2;  // 출금 요청
    int TYPE_WITHDRAW_SUCCESS = 3;  // 출금 성공
    int TYPE_WITHDRAW_DENIED = 4;          // 출금 거절(관리자)


    int TYPE_INTEREST = 6;          // 채굴 이자


    int TYPE_PRODUCT_PURCHASE_SUCCESS = 21; // 상품 가입 성공
    int TYPE_PRODUCT_PURCHASE_REMAIN = 22; // 상품 가입 나머지
    int TYPE_PRODUCT_CANCEL = 23;   // 상품 해지

    int TYPE_LF_DEPOSIT = 31;
    int TYPE_LF_UNLOCK = 32;


    PagingData selectPagingBySearchForApi(Map<String, Object> parameter);
    TransactionVOForApi selectBySearchForApi(Long idx);

    boolean isUniqueIdExist(Integer type, String uniqueId);
//    boolean addDeposit(MemberVO memberVO, MemberVO sender, TokenPointVO tokenPointVO, BigDecimal amount, String uniqueId, String txId, PaymentQueueVO paymentQueueVO);
    boolean addWithdrawRequest(MemberVO memberVO, TokenPointVO tokenPointVO, BigDecimal amount, String uniqueId, String to, BigDecimal feeRate, BigDecimal finalReturn);
    boolean addWithdrawSuccess(MemberVO memberVO, TokenPointVO tokenPointVO, BigDecimal amount, String to, String memo, String uniqueId, String txId);
    boolean addWithdrawDenied(MemberVO memberVO, TokenPointVO tokenPointVO, BigDecimal amount, String uniqueId);

    boolean addProductPurchaseSuccess(PaymentQueueVO paymentQueueVO, MemberVO memberVO, ProductInfoVO productInfoVO, BigDecimal amount);
    boolean addProductPurchaseRemain(PaymentQueueVO paymentQueueVO, MemberVO memberVO, ProductInfoVO productInfoVO, BigDecimal amount);
    boolean addProductCancel(ProductUserVO productUserVO, BigDecimal finalReturn, BigDecimal feeAmount, String feeRate);

    boolean addDailyInterest(ProductInfoVO productInfoVO, ProductUserVO productUserVO, BigDecimal dailyInterest, int interestIndex);

    boolean addLfDeposit(PaymentQueueVO paymentQueueVO, TokenInfoVO tokenInfoVO);
    boolean addLfUnlock(LfUserVO lfUserVO);



}
