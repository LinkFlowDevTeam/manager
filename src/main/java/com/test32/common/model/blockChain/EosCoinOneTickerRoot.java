package com.test32.common.model.blockChain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class EosCoinOneTickerRoot
{
    @JsonProperty("first")
    private BigDecimal first;

    @JsonProperty("last")
    private BigDecimal last;

    @JsonProperty("high")
    private BigDecimal high;

    @JsonProperty("low")
    private BigDecimal low;


    @JsonProperty("volume")
    private BigDecimal volume;
}