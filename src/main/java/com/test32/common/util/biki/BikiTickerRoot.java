package com.test32.common.util.biki;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class BikiTickerRoot
{
    @JsonProperty("code") private String code;
    @JsonProperty("msg") private String msg;
    @JsonProperty("message") private String message;
    @JsonProperty("data") BikiTickerData data;
}