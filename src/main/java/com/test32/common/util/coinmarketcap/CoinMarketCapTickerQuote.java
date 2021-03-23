package com.test32.common.util.coinmarketcap;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class CoinMarketCapTickerQuote
{
    @JsonProperty("KRW")
    private CoinMarketCapTickerQuoteValue krw;

    @JsonProperty("USD")
    private CoinMarketCapTickerQuoteValue usd;
}