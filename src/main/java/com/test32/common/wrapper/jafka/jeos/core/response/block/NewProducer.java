package com.test32.common.wrapper.jafka.jeos.core.response.block;

import lombok.Data;

import java.util.List;

/**
 * 
 * @author adyliu (imxylz@gmail.com)
 * @since Sep 26, 2018
 */
@Data
public class NewProducer {
    private String version;
    private List<Producer> producers;
}
