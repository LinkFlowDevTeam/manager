package com.test32.common.util.biki;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class BikiTickerData
{
    @JsonProperty("amount") private String amount;
    @JsonProperty("high") private String high;
    @JsonProperty("vol") private String vol;
    @JsonProperty("last") private String last;
    @JsonProperty("low") private String low;
    @JsonProperty("buy") private String buy;
    @JsonProperty("sell") private String sell;
    @JsonProperty("rose") private String rose;
    @JsonProperty("time") private String time;
}

//{
//        "code": "0",
//        "msg": "suc",
//        "data": {
//        "amount": "268150.8566694128",
//        "high": "0.99509",
//        "vol": "356648.452",
//        "last": 0.6725,
//        "low": "0.51226",
//        "buy": 0.6085,
//        "sell": 0.71999,
//        "rose": "-0.1005269775",
//        "time": 1602053722000
//        },
//        "message": null
//}