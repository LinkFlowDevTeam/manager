package com.test32.common.model.blockChain.ticker.cashierest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CashierestTickerAllData
{
    @JsonProperty("KRW_VII")
    private CashierestTickerAllDataKrwVii KRW_VII;
}