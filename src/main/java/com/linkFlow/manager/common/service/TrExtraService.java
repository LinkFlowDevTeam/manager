package com.linkFlow.manager.common.service;


import com.linkFlow.manager.common.model.vo.*;
import com.test32.common.generic.GenericServiceForBigInt;

import java.math.BigDecimal;

public interface TrExtraService extends GenericServiceForBigInt<TrExtraVO>
{
    boolean insertBy(long dataIdx, String data1, String data2, String data3, String data4, String data5, String data6);

//    boolean addProductPurchaseSuccess(PaymentQueueVO paymentQueueVO, MemberVO memberVO, ProductInfoVO productInfoVO, BigDecimal amount);
//    boolean addProductPurchaseRemain(PaymentQueueVO paymentQueueVO, MemberVO memberVO, ProductInfoVO productInfoVO, BigDecimal amount);
//    boolean addProductCancel(ProductUserVO productUserVO, BigDecimal finalReturn, BigDecimal feeAmount, String feeRate);
//
//    boolean addDailyInterest(ProductInfoVO productInfoVO, ProductUserVO productUserVO, BigDecimal dailyInterest, int interestIndex);
//
//    boolean addLfDeposit(PaymentQueueVO paymentQueueVO, TokenInfoVO tokenInfoVO);
//    boolean addLfUnlock(LfUserVO lfUserVO);

    boolean insertWithdrawRequest(long dataIdx, MemberVO memberVO, TokenPointVO tokenPointVO, BigDecimal amount, String uniqueId, String to, BigDecimal feeRate, BigDecimal finalReturn);
    boolean insertWithdrawSuccess(long dataIdx, MemberVO memberVO, TokenPointVO tokenPointVO, BigDecimal amount, String to, String memo, String uniqueId, String txId);

    boolean insertProductPurchaseSuccess(long dataIdx, PaymentQueueVO paymentQueueVO, MemberVO memberVO, ProductInfoVO productInfoVO, BigDecimal amount);
    boolean insertProductPurchaseRemain(long dataIdx, PaymentQueueVO paymentQueueVO, MemberVO memberVO, ProductInfoVO productInfoVO, BigDecimal amount);
    boolean insertProductCancel(long dataIdx, ProductUserVO productUserVO, BigDecimal finalReturn, BigDecimal feeAmount, String feeRate);

    boolean insertDailyInterest(long dataIdx, ProductInfoVO productInfoVO, ProductUserVO productUserVO, BigDecimal dailyInterest, int interestIndex);

    boolean insertLfDeposit(long dataIdx, PaymentQueueVO paymentQueueVO, TokenInfoVO tokenInfoVO);
    boolean insertLfUnlock(long dataIdx, LfUserVO lfUserVO);
}