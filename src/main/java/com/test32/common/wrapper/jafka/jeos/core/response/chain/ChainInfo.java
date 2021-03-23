package com.test32.common.wrapper.jafka.jeos.core.response.chain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChainInfo {

    private Long blockCpuLimit;
    private Long blockNetLimit;
    private String chainId;
    private String headBlockId;
    private Long headBlockNum;
    private String headBlockProducer;
    
    //@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime headBlockTime;
    
    private String lastIrreversibleBlockId;
    private Long lastIrreversibleBlockNum;
    private String serverVersion;
    private String serverVersionString;
    private Long virtualBlockCpuLimit;
    private Long virtualBlockNetLimit;
}
