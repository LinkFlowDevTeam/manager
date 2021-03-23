package com.test32.common.model.blockChain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class EosHuobiTickerRoot
{
    @JsonProperty("status")
    private String status;

    @JsonProperty("ch")
    private String ch;

    @JsonProperty("ts")
    private Long ts;

    @JsonProperty("tick")
    private EosHuobiTickerData tick;
}