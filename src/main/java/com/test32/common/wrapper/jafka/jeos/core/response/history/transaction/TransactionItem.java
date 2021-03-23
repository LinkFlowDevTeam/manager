package com.test32.common.wrapper.jafka.jeos.core.response.history.transaction;

import com.test32.common.wrapper.jafka.jeos.core.common.Action;
import lombok.Data;

import java.util.List;

/**
 * 
 * @author adyliu (imxylz@gmail.com)
 * @since 2018年8月29日
 */
@Data
public class TransactionItem {

    private Long delaySec;
    private String expiration;
    private Long maxCpuUsageMs;
    private Long maxNetUsageWords;
    private Long refBlockNum;
    private Long refBlockPrefix;
    private List<Action> actions;
    private List<Action> contextFreeActions;
}
