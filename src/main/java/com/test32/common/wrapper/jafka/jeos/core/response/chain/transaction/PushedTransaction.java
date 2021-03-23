package com.test32.common.wrapper.jafka.jeos.core.response.chain.transaction;

import lombok.Data;

@Data
public class PushedTransaction {
    private Processed processed;
    private String transactionId;
}
