package com.test32.common.model.blockChain.hyperion;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class HyperionSimpleActionsRoot
{
    @JsonProperty("query_time")
    private Long query_time;

    @JsonProperty("cached")
    private String cached;

    @JsonProperty("total")
    private HyperionSimpleActionsTotal total;

    @JsonProperty("simple_actions")
    private List<HyperionSimpleAction> simple_actions;
}