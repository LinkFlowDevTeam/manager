package com.test32.common.model.blockChain.hyperion;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class HyperionSimpleAction
{
    @JsonProperty("block")
    private Long block;
    @JsonProperty("timestamp")
    private String timestamp;
    @JsonProperty("irreversible")
    private Boolean irreversible;
    @JsonProperty("contract")
    private String contract;
    @JsonProperty("action")
    private String action;

    @JsonProperty("actors")
    private String actors;
    @JsonProperty("notified")
    private String notified;
    @JsonProperty("transaction_id")
    private String transaction_id;
}