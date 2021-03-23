package com.linkFlow.manager.admin.model.response;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class ResDashboardData
{
    private Integer tkIdx;
    private String tkSymbol;
    private String tkName;

    private BigDecimal deposit;
    private BigDecimal withdraw;
    private BigDecimal manualDeposit;
    private BigDecimal manualWithdraw;

    private Long countDeposit;
    private Long countWithdraw;
    private Long countManualDeposit;
    private Long countManualWithdraw;
}