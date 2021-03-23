package com.test32.common.wrapper.jafka.jeos.core.response.chain.account;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Wait {

    private Long weightSec;
    private Integer weight;
}
