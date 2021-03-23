package com.test32.common.model.blockChain.ticker.cashierest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CashierestTickerData
{
    @JsonProperty("ClosingPrice")
    private String ClosingPrice;

    @JsonProperty("OpeningPrice")
    private String OpeningPrice;

    @JsonProperty("MaxPrice")
    private String MaxPrice;

    @JsonProperty("MinPrice")
    private String MinPrice;


    @JsonProperty("UnitsTraded")
    private String UnitsTraded;

    @JsonProperty("BuyPrice")
    private String BuyPrice;

    @JsonProperty("SellPrice")
    private String SellPrice;

    @JsonProperty("CreateDate")
    private String CreateDate;
}