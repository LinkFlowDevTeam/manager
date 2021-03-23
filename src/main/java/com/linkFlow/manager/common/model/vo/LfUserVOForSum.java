package com.linkFlow.manager.common.model.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LfUserVOForSum
{
    private Integer countItem;
    private BigDecimal amount = BigDecimal.ZERO;
}
