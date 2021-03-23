package com.linkFlow.manager.api.model.request;

import lombok.Data;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;

@Data
public class ReqUserWithdraw extends ReqUserDefaultAuth
{
    @NotNull
    private String symbol;

    @NotNull
    private String to;

    @NotNull
    private String amount;

    private String memo;

    public void trimRequest()
    {
        if( ! StringUtils.isEmpty(this.getTo()))
            this.setTo(this.getTo().trim());
        if( ! StringUtils.isEmpty(this.getSymbol()))
            this.setSymbol(this.getSymbol().trim());
    }
}