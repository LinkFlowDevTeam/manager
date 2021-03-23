package com.test32.common.util.coinmarketcap;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class CoinMarketCapTickerData
{
    @JsonProperty("ETH")
    private CoinMarketCapTickerValue eth;

    @JsonProperty("USDT")
    private CoinMarketCapTickerValue usdt;

    @JsonProperty("EXE")
    private CoinMarketCapTickerValue exe;
}