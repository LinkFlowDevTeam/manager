package com.linkFlow.manager.common.model.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TokenPointVOForApi
{
    private Integer tokenId;
    private String symbol;
    private BigDecimal point;
    private BigDecimal lockedPoint;
}