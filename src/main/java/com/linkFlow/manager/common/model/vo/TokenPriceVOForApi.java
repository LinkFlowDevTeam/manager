package com.linkFlow.manager.common.model.vo;

import lombok.Data;

@Data
public class TokenPriceVOForApi
{
    private Integer tokenId;
    private String address;
    private Integer decimal;
    private String marketName;

    private String symbol;

    private String price;
    private String openPrice;
    private String highPrice;
    private String lowPrice;
}