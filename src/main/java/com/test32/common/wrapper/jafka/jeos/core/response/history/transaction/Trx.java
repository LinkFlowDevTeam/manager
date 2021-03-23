package com.test32.common.wrapper.jafka.jeos.core.response.history.transaction;

import lombok.Data;

import java.util.List;

/**
 * 
 * @author adyliu (imxylz@gmail.com)
 * @since 2018年8月29日
 */
@Data
public class Trx {

    private String compression;
    private String id;
    private String packedContextFreeData;
    private String packedTrx;
    private List<String> signatures;
    private TransactionItem transaction;
    
}
