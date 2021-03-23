package com.test32.common.util.coinmarketcap;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class CoinMarketCapChartDataQuoteItemKrw
{
    @JsonProperty("open") private String open;
    @JsonProperty("high") private String high;
    @JsonProperty("low") private String low;
    @JsonProperty("close") private String close;
    @JsonProperty("volume") private String volume;
    @JsonProperty("market_cap") private String market_cap;
    @JsonProperty("timestamp") private String timestamp;
}