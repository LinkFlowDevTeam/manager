package com.test32.common.model.blockChain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class EosBitThumbTickerData
{
    @JsonProperty("opening_price")
    private BigDecimal opening_price;

    @JsonProperty("closing_price")
    private BigDecimal closing_price;

    @JsonProperty("max_price")
    private BigDecimal max_price;

    @JsonProperty("min_price")
    private BigDecimal min_price;


    @JsonProperty("average_price")
    private BigDecimal average_price;

    @JsonProperty("buy_price")
    private BigDecimal buy_price;

    @JsonProperty("sell_price")
    private BigDecimal sell_price;


    @JsonProperty("units_traded")
    private BigDecimal units_traded;

    @JsonProperty("volume_1day")
    private BigDecimal volume_1day;

    @JsonProperty("volume_7day")
    private BigDecimal volume_7day;
}