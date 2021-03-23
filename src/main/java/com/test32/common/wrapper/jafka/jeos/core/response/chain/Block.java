package com.test32.common.wrapper.jafka.jeos.core.response.chain;

import com.test32.common.wrapper.jafka.jeos.core.response.block.NewProducer;
import com.test32.common.wrapper.jafka.jeos.core.response.history.transaction.Transaction;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Block {

    private String actionMroot;
    private Long blockNum;
    private Long confirmed;
    private String id;
    private String previous;
    private String producer;
    private String producerSignature;
    private Long refBlockPrefix;
    private Long scheduleVersion;
    private LocalDateTime timestamp;
    private String transactionMroot;

    private String actionMerkleRoot;

    private String blockMerkleRoot;

    private NewProducer newProducers;

    private Transaction[] transactions;

    private String[] headerExtensions;

    private String[] blockExtensions;
}
