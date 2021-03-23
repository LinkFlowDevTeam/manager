package com.test32.common.model.blockChain.ticker.bitsonic;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BitsonicTickerData
{
    @JsonProperty("e") private String e;
    @JsonProperty("E") private String E;
    @JsonProperty("p") private String p;
    @JsonProperty("P") private String P;
    @JsonProperty("x") private String x;
    @JsonProperty("s") private String s;
    @JsonProperty("i") private String i;
    @JsonProperty("o") private String o;
    @JsonProperty("c") private String c;
    @JsonProperty("h") private String h;
    @JsonProperty("l") private String l;
    @JsonProperty("v") private String v;
    @JsonProperty("q") private String q;
    @JsonProperty("t") private String t;
    @JsonProperty("T") private String T;
}