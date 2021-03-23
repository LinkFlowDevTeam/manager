package com.test32.common.model.blockChain.erc20.jsonRpc;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JsonRpcBlockNumber
{
    @JsonProperty("jsonrpc")
    private String jsonrpc;

    @JsonProperty("id")
    private Long id;

    @JsonProperty("result")
    private String result;
}
//{
//    "jsonrpc": "2.0",
//    "id": 1,
//    "result": "0x7a4c6f"
//}