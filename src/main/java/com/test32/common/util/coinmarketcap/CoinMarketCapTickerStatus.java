package com.test32.common.util.coinmarketcap;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class CoinMarketCapTickerStatus
{
    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("error_code")
    private String error_code;

    @JsonProperty("error_message")
    private String error_message;

    @JsonProperty("elapsed")
    private String elapsed;

    @JsonProperty("credit_count")
    private String credit_count;

    @JsonProperty("notice")
    private String notice;
}
//timestamp
//error_code
//error_message
//elapsed
//credit_count
//notice