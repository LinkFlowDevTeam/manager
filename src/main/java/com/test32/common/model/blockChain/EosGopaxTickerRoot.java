package com.test32.common.model.blockChain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class EosGopaxTickerRoot
{
    @JsonProperty("open")
    private BigDecimal open;

    @JsonProperty("close")
    private BigDecimal close;

    @JsonProperty("high")
    private BigDecimal high;

    @JsonProperty("low")
    private BigDecimal low;


    @JsonProperty("volume")
    private BigDecimal volume;
}