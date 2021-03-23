package com.linkFlow.manager.common.model.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TokenInfoVOForApi
{
    private Integer tokenId;
    private Integer order;

    private String address;
    private Integer decimal;
    private String marketName;

    private String symbol;
    private Integer state;

    private String price;
    private String openPrice;
    private String highPrice;
    private String lowPrice;

    private Integer lockWithdraw;
    private Integer lockPurchase;

    private BigDecimal minWithdrawAmount;
    private BigDecimal withdrawFee;
}