package com.test32.common.model.blockChain.erc20.jsonRpc;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JsonRpcGetTransactionByHashResult
{
    @JsonProperty("blockHash") private String blockHash;
    @JsonProperty("blockNumber") private String blockNumber;
    @JsonProperty("from") private String from;
    @JsonProperty("gas") private String gas;
    @JsonProperty("gasPrice") private String gasPrice;
    @JsonProperty("hash") private String hash;
    @JsonProperty("input") private String input;
    @JsonProperty("nonce") private String nonce;
    @JsonProperty("r") private String r;
    @JsonProperty("s") private String s;
    @JsonProperty("to") private String to;
    @JsonProperty("transactionIndex") private String transactionIndex;
    @JsonProperty("v") private String v;
}