package com.test32.common.model.blockChain.hyperion;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class HyperionGetTransactionActionRoot
{
    @JsonProperty("action_ordinal")
    private String action_ordinal;

    @JsonProperty("context_free")
    private String context_free;

    @JsonProperty("@timestamp")
    private String timestamp;

    @JsonProperty("block_num")
    private Long block_num;

    @JsonProperty("producer")
    private String producer;

    @JsonProperty("trx_id")
    private String trx_id;

    @JsonProperty("global_sequence")
    private Long global_sequence;

    //@JsonProperty("notified")
    //private List<String> notified;
}