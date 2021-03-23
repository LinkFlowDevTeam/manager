package com.linkFlow.manager.common.model.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MemberTierVOForApi
{
    private Integer tier;
    private Integer limitAmount;
    private Integer depositAmount;
    private BigDecimal rate;
}