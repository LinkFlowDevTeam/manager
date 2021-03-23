package com.linkFlow.manager.common.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.test32.common.converter.CustomDateTimeSerializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Erc20TxVO
{
    private Long etIdx;
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date etCreateDate;
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date etUpdateDate;
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date etTimestampDate;

    private Integer etState;

    // getTransactionByHash result
    private Integer etTxState;

    private String etTxHash;
    private String etTxValue;
    private String etTxInput;
    private Long etTxGas;
    private Long etTxGasPrice;
    private Integer etTxIndex;

    private BigDecimal etValue;

    private String etFrom;
    private String etTo;
    private String etBlockHash;
    private String etBlockString;
    private Long etBlockNumber;


    private String etRcContractAddress;
    private Integer etRcLogLength;
    private String etRcStatus;
    private Long etRcCumulativeGasUsed;
    private Long etRcGasUsed;
    private Long etRcGasLimit;


    // actual transfer
    private String etFunctionAddress;
    private String etActualTo;
    private BigDecimal etActualAmount;
    private String etActualString;
    private String etExtraMessage;


    // for additional search
    private Integer etFlagManager;
    private Integer etTkIdx;
}