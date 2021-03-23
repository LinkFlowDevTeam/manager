package com.test32.common.wrapper.jafka.jeos.core.response.history.transaction;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.util.Optional;

@Data
public class Transaction {
    private Long cpuUsageUs;
    private Long netUsageWords;
    private String status;
    
    @JsonDeserialize(using = TrxDeserializer.class)
    // nullable
    private Optional<Trx> trx;
}
