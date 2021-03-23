package com.linkFlow.manager.admin.model.request;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
public class ReqSendErc20
{
    private String symbol;
    private String type;
    private String from;
    private String to;

    private BigDecimal amount;
    private BigInteger gasLimit;
}