package com.test32.common.model.blockChain.ticker.bitsonic;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class BitsonicChartRoot
{
    @JsonProperty("function_name")
    private String function_name;

    @JsonProperty("result")
    private List<BitsonicChartData> result;
}