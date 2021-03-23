package com.test32.common.util.coinmarketcap;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class CoinMarketCapChartData
{
    @JsonProperty("id") private String id;
    @JsonProperty("name") private String name;
    @JsonProperty("symbol") private String symbol;

    @JsonProperty("quotes") private List<CoinMarketCapChartDataQuoteRoot> quotes;
}