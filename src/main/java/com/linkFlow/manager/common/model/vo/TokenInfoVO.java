package com.linkFlow.manager.common.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.test32.common.converter.CustomDateTimeSerializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TokenInfoVO
{
    private Integer tkIdx;
    private Integer tkOrder;
    private Integer tkPurpose;

    private String tkAddress;
    private Integer tkDecimal;
    private Integer tkRoundingPoint;
    private String tkMarketName;


    private String tkSymbol;
    private String tkPrice;
    private String tkOpenPrice;
    private String tkHighPrice;
    private String tkLowPrice;

    private Integer tkState;

    private Integer tkDefaultGeneration;
    private Integer tkLockWithdraw;
    private Integer tkAutoWithdraw;
    private Integer tkLockPurchase;

    private String tkWithdrawFee;
    private String tkMinWithdrawAmount;

    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date tkCreateDate;

    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date tkUpdateDate;


    private String tkUrlChart;
    private String tkChartData;
    private Integer tkChartState;
    private String tkUrlAllTicker;
    private String tkUrlTicker;
    private Integer tkTickerState;
    private String tkTickerSymbol;
    private String tkTickerChartResolution;
    private Integer tkTickerChartPeriod;


    private BigDecimal tkErc20Point;
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date tkErc20Date;
    private Integer tkErc20State;

    private String tkGasFeeDepositKrw;
}