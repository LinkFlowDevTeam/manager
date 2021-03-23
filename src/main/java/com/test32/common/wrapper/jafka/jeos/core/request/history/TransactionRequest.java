package com.test32.common.wrapper.jafka.jeos.core.request.history;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author adyliu (imxylz@gmail.com)
 * @since 2018年8月31日
 */
@Data
@RequiredArgsConstructor
public class TransactionRequest {
    
    @NonNull
    private String id;
    private Long blockNumHint;
}
