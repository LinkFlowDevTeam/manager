
package com.test32.common.wrapper.jafka.jeos.core.common;

import lombok.Data;

import java.util.List;

@Data
public class Action {
    private String account;
    private List<Authorization> authorization;
    private Object data;
    private String name;
    private String hexData;

}
