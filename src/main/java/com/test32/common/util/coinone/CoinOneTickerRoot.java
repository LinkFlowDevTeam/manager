package com.test32.common.util.coinone;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class CoinOneTickerRoot
{
    @JsonProperty("result") private String result;
    @JsonProperty("errorCode") private String errorCode;
    @JsonProperty("timestamp") private String timestamp;
    @JsonProperty("currency") private String currency;
    @JsonProperty("first") private String first;
    @JsonProperty("low") private String low;
    @JsonProperty("high") private String high;
    @JsonProperty("last") private String last;
    @JsonProperty("volume") private String volume;
}