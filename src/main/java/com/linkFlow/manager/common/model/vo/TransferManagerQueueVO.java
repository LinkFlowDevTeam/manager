package com.linkFlow.manager.common.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.test32.common.converter.CustomDateTimeSerializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TransferManagerQueueVO
{
    private Integer tqIdx;

    private String tqAddress;

    private String tqName;

    private Integer tqTkIdx;

    private String tqTkSymbol;

    private Integer tqState;

    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date tqCreateDate;

    private Long tqTargetIdx;

    private Integer tqTargetState;

    private String tqTargetType;

    private String tqTargetKey;

    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date tqRequestDate;

    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date tqFinishDate;

    private BigDecimal tqEthPoint;
    private BigDecimal tqErc20Point;

    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date tqErc20Date;


    private String tqData1;

    private String tqData2;

    private String tqData3;
}