package com.linkFlow.manager.common.model.vo;

import lombok.Data;

@Data
public class CoinmarketcapVO
{
    private Integer ckIdx;
    private Integer ckTickerState;
    private Integer ckChartState;

    private Integer ckTkIdx;
    private String ckTkSymbol;

    private String ckTickerUrl;
    private String ckChartUrl;

    private String ckKey;
    private String ckConvertCurrency;
}