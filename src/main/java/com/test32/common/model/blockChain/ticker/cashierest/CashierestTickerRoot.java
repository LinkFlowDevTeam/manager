package com.test32.common.model.blockChain.ticker.cashierest;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CashierestTickerRoot
{
    @JsonProperty("ErrCode")
    private Integer ErrCode;

    @JsonProperty("ErrMessage")
    private String ErrMessage;

    @JsonProperty("ReturnData")
    private CashierestTickerData returnData;
}