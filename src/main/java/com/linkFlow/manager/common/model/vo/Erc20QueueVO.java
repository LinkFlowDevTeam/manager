package com.linkFlow.manager.common.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.test32.common.converter.CustomDateTimeSerializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Erc20QueueVO
{
    private Long eqIdx;

    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date eqCreateDate;

    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date eqResponseDate;

    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date eqUpdateDate;

    private Integer eqState;
    private Long eqMbIdx;
    private Integer eqIo;
    private Long eqNonce;
    private Long eqGasLimit;
    private Long eqGasPrice;
    private String eqContractAddress;
    private String eqFrom;
    private String eqTo;
    private BigDecimal eqAmount;
    private Integer eqTkIdx;
    private String eqSymbol;
    private String eqExtraMessage;

    // response
    private String eqStatus;
    private String eqGasUsed;
    private Long eqBlockNumber;
    private String eqBlockString;
    private String eqBlockHash;
    private String eqTxHash;


    // update
    private Integer eqChainState;
    private String eqChainMessage;


    // join
    private MemberVO memberVO;
}