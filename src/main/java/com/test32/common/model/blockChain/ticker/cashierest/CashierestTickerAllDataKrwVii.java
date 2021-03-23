package com.test32.common.model.blockChain.ticker.cashierest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CashierestTickerAllDataKrwVii
{
    @JsonProperty("id")
    private String id;

    @JsonProperty("percentChange")
    private String percentChange;
}