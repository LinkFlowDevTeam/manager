package com.test32.common.model.blockChain.ticker.bitsonic;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BitsonicChartData
{
    @JsonProperty("timestamp")
    private Long timestamp;

    @JsonProperty("open")
    private String open;

    @JsonProperty("low")
    private String low;

    @JsonProperty("high")
    private String high;

    @JsonProperty("close")
    private String close;


    @JsonProperty("base_volume")
    private String base_volume;

    @JsonProperty("volume")
    private String volume;
}