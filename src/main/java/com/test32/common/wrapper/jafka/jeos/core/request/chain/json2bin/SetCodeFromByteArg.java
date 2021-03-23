package com.test32.common.wrapper.jafka.jeos.core.request.chain.json2bin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SetCodeFromByteArg
{
    private String account;
    private Integer vmtype;
    private Integer vmversion;
    private byte[] code;
}
