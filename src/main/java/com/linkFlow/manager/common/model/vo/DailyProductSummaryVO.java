package com.linkFlow.manager.common.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.test32.common.converter.CustomDateTimeSerializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class DailyProductSummaryVO
{
    private Integer dsIdx;
    private String dsYear;
    private String dsMonth;

    private String dsDate;

    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dsUpdateDate;

    private Integer dsFlagFix;
    private Integer dsTkIdx;
    private String dsTkSymbol;

    private BigDecimal dsSumAccumulatedTotal;
    private BigDecimal dsSumAccumulatedBase;
    private BigDecimal dsSumAccumulatedInterest;


    private Integer dsCountDeposit;
    private BigDecimal dsSumDeposit;

    private Integer dsCountInterest;
    private BigDecimal dsSumInterest;

    private Integer dsCountWithdraw;
    private BigDecimal dsSumWithdraw;

    private Integer dsCountFee;
    private BigDecimal dsSumFee;
}