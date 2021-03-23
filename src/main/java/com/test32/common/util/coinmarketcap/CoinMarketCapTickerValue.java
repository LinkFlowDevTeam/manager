package com.test32.common.util.coinmarketcap;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class CoinMarketCapTickerValue
{
    @JsonProperty("id") private Integer id;
    @JsonProperty("name") private String name;
    @JsonProperty("symbol") private String symbol;
    @JsonProperty("slug") private String slug;
    @JsonProperty("num_market_pairs") private Integer num_market_pairs;

    @JsonProperty("circulating_supply") private BigDecimal circulating_supply;
    @JsonProperty("total_supply") private BigDecimal total_supply;

    @JsonProperty("quote")
    private CoinMarketCapTickerQuote quote;
}
//"id": 1765,
//"name": "EOS",
//"symbol": "EOS",
//"slug": "eos",
//"num_market_pairs": 376,
//"date_added": "2017-07-01T00:00:00.000Z",
//"tags": [],
//"max_supply": null,
//"circulating_supply": 933587660.1163,
//"total_supply": 1020287671.4106,
//"is_active": 1,
//"platform": null,
//"cmc_rank": 9,
//"is_fiat": 0,
//"last_updated": "2020-06-18T09:12:05.000Z",
//"quote": {
//"KRW": {
//"price": 3089.5868467600267,
//"volume_24h": 1520310313082.3135,
//"percent_change_1h": -0.20787912,
//"percent_change_24h": -0.88102458,
//"percent_change_7d": -6.11162706,
//"market_cap": 2884400154992.7905,
//"last_updated": "2020-06-18T09:13:00.000Z"
//}