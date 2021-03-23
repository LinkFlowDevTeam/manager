package com.test32.common.model.blockChain.erc20.jsonRpc;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JsonRpcGetTransactionByHash
{
    @JsonProperty("jsonrpc")
    private String jsonrpc;

    @JsonProperty("id")
    private Long id;

    @JsonProperty("result")
    private JsonRpcGetTransactionByHashResult result;
}
//{
//    "jsonrpc": "2.0",
//    "id": 1,
//    "result": {
//        "blockHash": "0xe647e62268d2147f5c16b39e4900d471e7f5323664984d02b42c112196031d61",
//        "blockNumber": "0x79d1ec",
//        "from": "0x687422eea2cb73b5d3e242ba5456b782919afc85",
//        "gas": "0x4cb26",
//        "gasPrice": "0x77359400",
//        "hash": "0x51ea1e3bb57164a88cb45bfe3e62f39842105e13efc165e0d62073b9a23dec1d",
//        "input": "0x",
//        "nonce": "0x173a1b",
//        "r": "0x3ca54b629962f7a9b3b77a601fb8f09e0935af22cdd5a2a707074103336ee5d2",
//        "s": "0x6380a5aa636a233f913c790fa70486a0d6877aeca05fb5bcd001cac1bb7792cf",
//        "to": "0x1479a9bfa1ee3c33956ea946827a6d9f3a96dc5b",
//        "transactionIndex": "0x3",
//        "v": "0x1b",
//        "value": "0xde0b6b3a7640000"
//    }
//}