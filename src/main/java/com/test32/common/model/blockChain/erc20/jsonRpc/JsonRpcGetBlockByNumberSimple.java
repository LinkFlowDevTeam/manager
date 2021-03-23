package com.test32.common.model.blockChain.erc20.jsonRpc;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JsonRpcGetBlockByNumberSimple
{
    @JsonProperty("jsonrpc")
    private String jsonrpc;

    @JsonProperty("id")
    private Long id;

    @JsonProperty("result")
    private JsonRpcGetBlockByNumberSimpleResult result;
}
//{
//    "jsonrpc": "2.0",
//    "id": 1,
//    "result": {
//        "difficulty": "0x17febf132",
//        "extraData": "0xde830204098f5061726974792d457468657265756d86312e33352e30826c69",
//        "gasLimit": "0x7a121d",
//        "gasUsed": "0x4cbbb",
//        "hash": "0xd05a142a91b7e3abd807e0ed1ff8bdf43e6c251763ab436a1a59aea824f7c10e",
//        "logsBloom": "0x00400000100000100000000000000000000000020000000000000000000000000000000000000800000000000000000000000000000000000000000000140000000000000400200800200008000200000000000000040000000000000000000000000004020000000000002000000801000000000000000000000010040000040000000000000000000000200008200008000100000000000000000000100000000000000000001000000000000000000000000000000020000000000020020000000002000001000000000000000008001000000000000000000000000060000000200000000000000000000000000000040000000000000008000000000004",
//        "miner": "0x48fd35f5dc1c4fd43b3e2a2f1fe0d18e7e4bcf2d",
//        "mixHash": "0xe834fe441d134ce8f6354a2d082aabf89c6c0fea6acf9ca1f81a45c6560cd078",
//        "nonce": "0xa56808000596494e",
//        "number": "0x5bad55",
//        "parentHash": "0x6b3dd319681e09a28f635c5d505af36213792173c93116cae22736b1974ebb67",
//        "receiptsRoot": "0xc5367f593fc3a40c003007b931ad06956a5476a66e7969f961c3fb2946ea60df",
//        "sha3Uncles": "0x4e509026bb6b9044f16f52cb4b463cdc46c5bb1126f0620267741da7c11b8380",
//        "size": "0x7ee",
//        "stateRoot": "0x79e1a1ba5e1f42feb8043b8c923332d11ff6bf81efd8646fef079d52bd3ba5bb",
//        "timestamp": "0x5d2f8aa3",
//        "totalDifficulty": "0x478e1824681a86",
//        "transactions": [
//            "0xfe388dc47891f08e72f92017173469ea76d63c17d3af06200946d184a6465bc3",
//            "0xb95326ec24ff62a0db0b2a2f02f858ec8db2ef619e0e495d1d72c75d5e9e5628",
//            "0x1e3ff6ba393f14c59a894bc18452a7ba49c60590e946a02aad1f4c164268c095",
//            "0x7efe645ece4183a70f05fb31f717116a9033137207871d4eac4809b93076958d"
//        ],
//        "transactionsRoot": "0xb13e00215b10cdd735702cd7895908728ace7fa16fee6172392674b181adba2c",
//        "uncles": [
//            "0x0d2f1aab0d910ea33759d0cec9b0b72e3dc57b8ba5351125eb3c1766e95513cb"
//        ]
//    }
//}
