package com.test32.common.util.coinmarketcap;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class CoinMarketCapTickerQuoteValue
{
    @JsonProperty("price") private BigDecimal price;
    @JsonProperty("volume_24h") private BigDecimal volume_24h;
    @JsonProperty("percent_change_1h") private BigDecimal percent_change_1h;
    @JsonProperty("percent_change_24h") private BigDecimal percent_change_24h;
    @JsonProperty("percent_change_7d") private BigDecimal percent_change_7d;
    @JsonProperty("market_cap") private BigDecimal market_cap;
    @JsonProperty("last_updated") private String last_updated;
}
//"KRW": {
//"price": 3089.5868467600267,
//"volume_24h": 1520310313082.3135,
//"percent_change_1h": -0.20787912,
//"percent_change_24h": -0.88102458,
//"percent_change_7d": -6.11162706,
//"market_cap": 2884400154992.7905,
//"last_updated": "2020-06-18T09:13:00.000Z"
//}