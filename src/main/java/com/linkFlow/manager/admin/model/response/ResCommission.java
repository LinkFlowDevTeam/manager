package com.linkFlow.manager.admin.model.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ResCommission {

    private BigDecimal commissionRemainAmount = BigDecimal.ZERO;

    private BigDecimal swapOutAmount = BigDecimal.ZERO;
    private Integer swapOutCount = 0;

    private BigDecimal interestAmount = BigDecimal.ZERO;
    private Integer interestCount = 0;

    private BigDecimal groupBonusAmount = BigDecimal.ZERO;
    private Integer groupBonusCount = 0;

    private BigDecimal dailyMatchingAmount = BigDecimal.ZERO;
    private Integer dailyMatchingCount = 0;

    private BigDecimal globalBonusAmount = BigDecimal.ZERO;
    private Integer globalBonusCount = 0;
}
