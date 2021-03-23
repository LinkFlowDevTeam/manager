package com.test32.common.model.blockChain.erc20.jsonRpc;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JsonRpcBalanceOf
{
    @JsonProperty("jsonrpc")
    private String jsonrpc;

    @JsonProperty("id")
    private String id;

    @JsonProperty("result")
    private String result;
}