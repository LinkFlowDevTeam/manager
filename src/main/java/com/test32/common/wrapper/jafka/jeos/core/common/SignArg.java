package com.test32.common.wrapper.jafka.jeos.core.common;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * some args for signature
 * 
 * @author adyliu (imxylz@gmail.com)
 * @since 2018年9月9日
 */
@Data
public class SignArg {

    Long headBlockNum;
    Long lastIrreversibleBlockNum;
    Long refBlockPrefix;
    LocalDateTime headBlockTime;
    String chainId;
    //
    int expiredSecond;

}
