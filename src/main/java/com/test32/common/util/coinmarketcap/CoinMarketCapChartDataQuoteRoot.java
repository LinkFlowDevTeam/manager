package com.test32.common.util.coinmarketcap;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class CoinMarketCapChartDataQuoteRoot
{
    @JsonProperty("time_open") private String time_open;
    @JsonProperty("time_close") private String time_close;
    @JsonProperty("time_high") private String time_high;
    @JsonProperty("time_low") private String time_low;

    @JsonProperty("quote") private CoinMarketCapChartDataQuoteItem quote;
}