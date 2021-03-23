package com.test32.common.model.blockChain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CommonTickerData
{
    @JsonProperty("start")
    private BigDecimal start;

    @JsonProperty("last")
    private BigDecimal last;

    @JsonProperty("high")
    private BigDecimal high;

    @JsonProperty("low")
    private BigDecimal low;


    @JsonProperty("volume")
    private BigDecimal volume;
}