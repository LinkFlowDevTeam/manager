package com.test32.common.model.blockChain.erc20.jsonRpc;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JsonRpcGetBlockByNumberDetailResultTransaction
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
    @JsonProperty("value") private String value;
}
//{
//                "blockHash": "0xd05a142a91b7e3abd807e0ed1ff8bdf43e6c251763ab436a1a59aea824f7c10e",
//                "blockNumber": "0x5bad55",
//                "from": "0xab81c4864b69838357875e4d273a8b692117bbc2",
//                "gas": "0x30d40",
//                "gasPrice": "0xdf8475800",
//                "hash": "0xfe388dc47891f08e72f92017173469ea76d63c17d3af06200946d184a6465bc3",
//                "input": "0x319c6b8700000000000000000000000000000000000000000000000000000000000000d5",
//                "nonce": "0x493e",
//                "r": "0xacad4ce94724a1d2d24412365767ac374bc4c4ccc0e236e53d53c8825dba0cb6",
//                "s": "0x4e8bd87e5061cb06d2ce16684b094095130a0642bd9c994a51e3bc1fe01525ba",
//                "to": "0xe71eeefd5daec62dacca343b7eeeb91acc76215c",
//                "transactionIndex": "0x0",
//                "v": "0x29",
//                "value": "0x0"
//            }