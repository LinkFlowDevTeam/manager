package com.test32.common.model.blockChain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class EosUpbitTickerRoot
{
    @JsonProperty("opening_price")
    private BigDecimal opening_price;

    @JsonProperty("trade_price")
    private BigDecimal trade_price;

    @JsonProperty("high_price")
    private BigDecimal high_price;

    @JsonProperty("low_price")
    private BigDecimal low_price;


    @JsonProperty("acc_trade_volume")
    private BigDecimal acc_trade_volume;
}