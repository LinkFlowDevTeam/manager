package com.linkFlow.manager.common.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.test32.common.converter.CustomDateTimeSerializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class Erc20BlockVO
{
    private Long ebIdx;
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date ebCreateDate;

    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date ebUpdateDate;
    private Integer ebState;

    private Long ebGasLimit;
    private Long ebGasUsed;

    private String ebBlockHash;
    private Long ebBlockNumber;
    private String ebBlockString;

    private Integer ebTxRawCount;
    private Integer ebTxValidCount;


    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date ebTimestampDate;
    private String ebTimestampString;
}