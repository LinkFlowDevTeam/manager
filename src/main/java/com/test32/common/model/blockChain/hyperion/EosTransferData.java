package com.test32.common.model.blockChain.hyperion;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class EosTransferData
{
    @JsonProperty("from")
    private String from;

    @JsonProperty("to")
    private String to;

    @JsonProperty("amount")
    private BigDecimal amount;

    @JsonProperty("symbol")
    private String symbol;

    @JsonProperty("memo")
    private String memo;

    @JsonProperty("quantity")
    private String quantity;
}