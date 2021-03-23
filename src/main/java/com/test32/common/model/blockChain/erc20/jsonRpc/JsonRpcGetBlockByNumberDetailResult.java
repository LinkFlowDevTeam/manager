package com.test32.common.model.blockChain.erc20.jsonRpc;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JsonRpcGetBlockByNumberDetailResult
{
    @JsonProperty("difficulty") private String difficulty;
    @JsonProperty("extraData") private String extraData;
    @JsonProperty("gasLimit") private String gasLimit;
    @JsonProperty("gasUsed") private String gasUsed;
    @JsonProperty("hash") private String hash;
    @JsonProperty("logsBloom") private String logsBloom;
    @JsonProperty("miner") private String miner;
    @JsonProperty("mixHash") private String mixHash;
    @JsonProperty("nonce") private String nonce; //Null when the returned block is the pending block.
    @JsonProperty("number") private String number;
    @JsonProperty("parentHash") private String parentHash;
    @JsonProperty("receiptsRoot") private String receiptsRoot;
    @JsonProperty("sha3Uncles") private String sha3Uncles;
    @JsonProperty("size") private String size;
    @JsonProperty("stateRoot") private String stateRoot;
    @JsonProperty("timestamp") private String timestamp;
    @JsonProperty("totalDifficulty") private String totalDifficulty;
    @JsonProperty("transactions") private JsonRpcGetBlockByNumberDetailResultTransaction[] transactions;
    @JsonProperty("transactionsRoot") private String transactionsRoot;
    @JsonProperty("uncles") private String[] uncles;
}

//BLOCK - A block object, or null when no block was found
//number: the block number. Null when the returned block is the pending block.
//hash: 32 Bytes - hash of the block. Null when the returned block is the pending block.
//parentHash: 32 Bytes - hash of the parent block.
//nonce: 8 Bytes - hash of the generated proof-of-work. Null when the returned block is the pending block.
//sha3Uncles: 32 Bytes - SHA3 of the uncles data in the block.
//logsBloom: 256 Bytes - the bloom filter for the logs of the block. Null when the returned block is the pending block.
//transactionsRoot: 32 Bytes - the root of the transaction trie of the block.
//stateRoot: 32 Bytes - the root of the final state trie of the block.
//receiptsRoot: 32 Bytes - the root of the receipts trie of the block.
//miner: 20 Bytes - the address of the beneficiary to whom the mining rewards were given.
//difficulty: integer of the difficulty for this block.
//totalDifficulty: integer of the total difficulty of the chain until this block.
//extraData: the "extra data" field of this block.
//size: integer the size of this block in bytes.
//gasLimit: the maximum gas allowed in this block.
//gasUsed: the total used gas by all transactions in this block.
//timestamp: the unix timestamp for when the block was collated.
//transactions: Array - Array of transaction objects, or 32 Bytes transaction hashes depending on the last given parameter.
//uncles: an Array of uncle hashes.