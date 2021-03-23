package com.test32.common.model.blockChain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class ChainGetInfo
{
@JsonProperty("server_version") private String server_version;
@JsonProperty("chain_id") private String chain_id;
@JsonProperty("head_block_num") private String head_block_num;
@JsonProperty("last_irreversible_block_num") private String last_irreversible_block_num;
@JsonProperty("last_irreversible_block_id") private String last_irreversible_block_id;
@JsonProperty("head_block_id") private String head_block_id;
@JsonProperty("head_block_time") private String head_block_time;
@JsonProperty("head_block_producer") private String head_block_producer;
@JsonProperty("virtual_block_cpu_limit") private String virtual_block_cpu_limit;
@JsonProperty("virtual_block_net_limit") private String virtual_block_net_limit;
@JsonProperty("block_cpu_limit") private String block_cpu_limit;
@JsonProperty("block_net_limit") private String block_net_limit;
@JsonProperty("server_version_string") private String server_version_string;
@JsonProperty("fork_db_head_block_num") private String fork_db_head_block_num;
@JsonProperty("fork_db_head_block_id") private String fork_db_head_block_id;
@JsonProperty("server_full_version_string") private String server_full_version_string;
}