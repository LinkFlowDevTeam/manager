package com.test32.common.model.blockChain.erc20.jsonRpc;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JsonRpcGetTransactionReceiptResult
{
    @JsonProperty("blockHash") private String blockHash;
    @JsonProperty("blockNumber") private String blockNumber;
    @JsonProperty("contractAddress") private String contractAddress;
    @JsonProperty("cumulativeGasUsed") private String cumulativeGasUsed;
    @JsonProperty("from") private String from;
    @JsonProperty("gasUsed") private String gasUsed;
    //@JsonProperty("logs") private String[] logs;
    //@JsonProperty("logsBloom") private String[] logsBloom;
    @JsonProperty("status") private String status;
    @JsonProperty("to") private String to;
    @JsonProperty("transactionHash") private String transactionHash;
    @JsonProperty("transactionIndex") private String transactionIndex;
}
//{"jsonrpc":"2.0","id":1,"result":{"blockHash":"0x32fc541af4680cb9f74cb64e137594ac65aa2ced8a66377246d18118ff9c5e27","blockNumber":"0x7ad93b","contractAddress":null,"cumulativeGasUsed":"0x368c9e","from":"0x1479a9bfa1ee3c33956ea946827a6d9f3a96dc5b","gasUsed":"0x8fd1","logs":[{"address":"0x413d7270ba9cfbce4bdaa2b2dac38bdc5e40c52d","blockHash":"0x32fc541af4680cb9f74cb64e137594ac65aa2ced8a66377246d18118ff9c5e27","blockNumber":"0x7ad93b","data":"0x0000000000000000000000000000000000000000000000000de0b6b"[truncated 1085 chars]; line: 1, column: 274] (through reference chain: com.test32.common.model.blockChain.erc20.jsonRpc.JsonRpcGetTransactionReceipt["result"]->com.test32.common.model.blockChain.erc20.jsonRpc.JsonRpcGetTransactionReceiptResult["logs"]->java.lang.Object[][0])