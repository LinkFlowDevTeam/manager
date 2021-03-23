package com.test32.common.model.blockChain.ticker.bitsonic;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BitsonicTickerRoot
{
    @JsonProperty("return_code") private Integer return_code;
    @JsonProperty("timestamp") private Integer timestamp;
    @JsonProperty("http_code") private Integer http_code;

    @JsonProperty("result") private BitsonicTickerData result;
}