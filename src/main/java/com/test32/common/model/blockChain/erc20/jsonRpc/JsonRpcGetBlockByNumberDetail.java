package com.test32.common.model.blockChain.erc20.jsonRpc;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JsonRpcGetBlockByNumberDetail
{
    @JsonProperty("jsonrpc")
    private String jsonrpc;

    @JsonProperty("id")
    private Long id;

    @JsonProperty("result")
    private JsonRpcGetBlockByNumberDetailResult result;
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
//            {
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
//            },
//            {
//                "blockHash": "0xd05a142a91b7e3abd807e0ed1ff8bdf43e6c251763ab436a1a59aea824f7c10e",
//                "blockNumber": "0x5bad55",
//                "from": "0x8332fc0fa6ca0004ed6d81a55599ad0dc43c5b92",
//                "gas": "0x36d35",
//                "gasPrice": "0x2540be400",
//                "hash": "0xb95326ec24ff62a0db0b2a2f02f858ec8db2ef619e0e495d1d72c75d5e9e5628",
//                "input": "0x08a8d7aa0000000000000000000000008332fc0fa6ca0004ed6d81a55599ad0dc43c5b920000000000000000000000000000000000000000000000000000000000000010000000000000000000000000000000000000000000000000000000000000008000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000013323031392f30362f30322032323a32373a303000000000000000000000000000",
//                "nonce": "0x29baf",
//                "r": "0xa4bace0cdf371f03c2d0d85a995cd8608638600d07d7171464fc6b54d6f4e8c6",
//                "s": "0x53666c6a7a9741704e477be5fa2d280beb32b1529972c8ec6cf2d94c8bdb8b36",
//                "to": "0xb57b6a10fd5d32e89b0cd93b7ffed0da2f734f58",
//                "transactionIndex": "0x1",
//                "v": "0x2a",
//                "value": "0x0"
//            },
//            {
//                "blockHash": "0xd05a142a91b7e3abd807e0ed1ff8bdf43e6c251763ab436a1a59aea824f7c10e",
//                "blockNumber": "0x5bad55",
//                "from": "0x93f916f1cc5f074bff9e63c4db7c0ddbebd8c263",
//                "gas": "0x395aa",
//                "gasPrice": "0x2540be400",
//                "hash": "0x1e3ff6ba393f14c59a894bc18452a7ba49c60590e946a02aad1f4c164268c095",
//                "input": "0x67d62fcf00000000000000000000000093f916f1cc5f074bff9e63c4db7c0ddbebd8c263000000000000000000000000000000000000000000000000000000000000000e00000000000000000000000000000000000000000000000000000000000000a0000000000000000000000000000000000000000000000000000000000000000100000000000000000000000000000000000000000000000000000000000000e00000000000000000000000000000000000000000000000000000000000000013323031392f30362f30322032323a32373a3030000000000000000000000000000000000000000000000000000000000000000000000000000000000000000013323031392f30362f30322032323a30303a303000000000000000000000000000",
//                "nonce": "0x221e1",
//                "r": "0xda406e526b1d352a90891aef769053daff28d720cee1fe9be3d813b84e23ae9",
//                "s": "0x40c33edf92cbcf72788243a13b1711daf8f7d1c5c3318836140f2054b1fad830",
//                "to": "0xb57b6a10fd5d32e89b0cd93b7ffed0da2f734f58",
//                "transactionIndex": "0x2",
//                "v": "0x29",
//                "value": "0x0"
//            },
//            {
//                "blockHash": "0xd05a142a91b7e3abd807e0ed1ff8bdf43e6c251763ab436a1a59aea824f7c10e",
//                "blockNumber": "0x5bad55",
//                "from": "0xff2c1e753bfb8812c1cace9b28fcb43aefddb623",
//                "gas": "0x5208",
//                "gasPrice": "0x2540be400",
//                "hash": "0x7efe645ece4183a70f05fb31f717116a9033137207871d4eac4809b93076958d",
//                "input": "0x",
//                "nonce": "0x7da",
//                "r": "0x9bca1227ba8b30f8c9a137c1d68551a8695c79e7c4b1e79e0e7c4385ae77fc7d",
//                "s": "0x4ca936b6334bef161a08587c1e3ea7ae69d0e89935c7ee7e51e41894859aaaad",
//                "to": "0xcfb5896a29820a4571cff44f2a31ac77a419e616",
//                "transactionIndex": "0x3",
//                "v": "0x29",
//                "value": "0x6f04f69ecbb8e000"
//            }
//        ],
//        "transactionsRoot": "0xb13e00215b10cdd735702cd7895908728ace7fa16fee6172392674b181adba2c",
//        "uncles": [
//            "0x0d2f1aab0d910ea33759d0cec9b0b72e3dc57b8ba5351125eb3c1766e95513cb"
//        ]
//    }
//}