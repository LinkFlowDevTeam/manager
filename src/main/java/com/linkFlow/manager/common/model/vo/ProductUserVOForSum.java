package com.linkFlow.manager.common.model.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductUserVOForSum
{
    private Integer countItem;
    private BigDecimal amount = BigDecimal.ZERO;
}
