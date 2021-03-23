package com.test32.common.wrapper.jafka.jeos.core.common;

import lombok.Data;

import java.util.List;

@Data
public class Receipt {

    private Long abiSequence;

    private String actDigest;

    private List<List<String>> authSequence = null;

    private Long codeSequence;

    private Long globalSequence;

    private String receiver;

    private Long recvSequence;
}
