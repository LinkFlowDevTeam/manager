package com.linkFlow.manager.common.model.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionVOForSum
{
    private Integer countItem;

    private BigDecimal sumAmount;
}