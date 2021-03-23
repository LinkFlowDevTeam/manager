package com.linkFlow.manager.common.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.test32.common.converter.CustomDateTimeSerializer;
import lombok.Data;
import org.apache.ibatis.type.Alias;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Alias("PaymentQueueVO")
public class PaymentQueueVO
{
    private Long pqIdx;
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date pqCreateDate;

    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date pqUpdateDate;


    // unique
    private Integer pqPaymentType;
    private String pqUniqueId;
    private Long pqMbIdx;

    // 내부전송때문에, 쪼인 힘듬..
    private Long pqToMbIdx;
    private String pqToMbId;
    private String pqToMbName;

    // 내부전송
    private Integer pqTrFlag;

    // request info
    private String pqSendFrom;
    private String pqSendTo;
    private String pqSendMemo;


    private Integer pqState;
    private Integer pqTkIdx;
    private String pqSymbol;
    private BigDecimal pqAmount;
    private BigDecimal pqReqAmount;
    private BigDecimal pqFee;
    private BigDecimal pqBefore;
    private BigDecimal pqAfter;


    // for transaction result
    private String pqResultData;

    // for data order
    private String pqSequence;

    private String pqEtTo;
    private String pqEtFrom;
    private String pqEtActualTo;
    private String pqTimestamp;


    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date pqRqDate;
    private Integer pqErrorCount;
    private Integer pqCkState;


    private Integer pqTxState;
    private Integer pqRsState;

    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date pqTxDate;

    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date pqRsDate;

    private Long pqBlockNumber;
    private String pqTxHash;
    private Integer pqGasLimit;
    private Long pqGasPrice;
    private Long pqNonce;


    // join
    private TokenInfoVO tokenInfoVO;
    private MemberVO memberVO;
}
