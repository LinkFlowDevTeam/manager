package com.linkFlow.manager.common.model.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DailyProductSummaryVOForApi
{
    private String month;
    private String date;

    private Integer tokenId;
    private String symbol;

    private BigDecimal sumAccumulatedTotal;
    private BigDecimal sumAccumulatedBase;
    private BigDecimal sumAccumulatedInterest;


    private Integer countDeposit;
    private BigDecimal sumDeposit;

    private Integer countInterest;
    private BigDecimal sumInterest;
}