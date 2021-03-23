package com.test32.common.model.blockChain.erc20.jsonRpc;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JsonRpcGetTransactionReceipt
{
    @JsonProperty("jsonrpc")
    private String jsonrpc;

    @JsonProperty("id")
    private Long id;

    @JsonProperty("result")
    private JsonRpcGetTransactionReceiptResult result;
}
//{
//    "jsonrpc": "2.0",
//    "id": 1,
//    "result": {
//        "blockHash": "0x60cb654ed3bf8a2bd9ba5aec309a05c9f780d5dc42f4c7791a6999b7e69f270a",
//        "blockNumber": "0x79d1dc",
//        "contractAddress": null,
//        "cumulativeGasUsed": "0x352bfd",
//        "from": "0x1479a9bfa1ee3c33956ea946827a6d9f3a96dc5b",
//        "gasUsed": "0x5208",
//        "logs": [],
//        "logsBloom": "0x00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000",
//        "status": "0x1",
//        "to": "0x0607c1d152c30447133bc50b009afd475061befb",
//        "transactionHash": "0x827a90c0752118b5f6a781e061a8ebbbe74640c55dadc4f8d0602c6d69ce9186",
//        "transactionIndex": "0x9"
//    }
//}