package com.test32.common.model.blockChain.ticker.cashierest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class CashierestTickerAllRoot
{
    @JsonProperty("Cashierest")
    private CashierestTickerAllData Cashierest;
}