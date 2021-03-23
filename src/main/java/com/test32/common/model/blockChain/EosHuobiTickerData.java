package com.test32.common.model.blockChain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class EosHuobiTickerData
{
    @JsonProperty("open")
    private BigDecimal open;

    @JsonProperty("close")
    private BigDecimal close;

    @JsonProperty("high")
    private BigDecimal high;

    @JsonProperty("low")
    private BigDecimal low;


    @JsonProperty("vol")
    private BigDecimal vol;

    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("count")
    private String count;


    @JsonProperty("version")
    private BigDecimal version;

    @JsonProperty("id")
    private BigDecimal id;
}