package com.test32.common.model.blockChain.hyperion;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class HyperionGetTransactionRoot
{
    @JsonProperty("trx_id")
    private String trx_id;

    @JsonProperty("lib")
    private String lib;


    @JsonProperty("actions")
    private List<HyperionGetTransactionActionRoot> actions;
}