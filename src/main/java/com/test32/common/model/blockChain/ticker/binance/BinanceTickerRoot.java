package com.test32.common.model.blockChain.ticker.binance;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BinanceTickerRoot
{
    @JsonProperty("symbol")
    private String symbol;

    @JsonProperty("price")
    private String price;
}
//{"symbol":"ETHTUSD","price":"344.88000000"}