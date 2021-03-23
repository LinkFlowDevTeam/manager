package com.linkFlow.manager.common.model.slack;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.test32.common.converter.CustomDateTimeSerializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class SlackMessageAttachmentWithdrawError
{
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date;
    private String type;
    private String userId;
    private String userName;

    @JsonProperty("출금량") private String quantity;
    @JsonProperty("출금주소") private String from;
    @JsonProperty("출금주소") private String to;
    private String memo;
}

// 출금 오류,sevenluckytt,
// kiru123kiru,10.0000 VII,
// aa,com.test32.common.wrapper.jafka.jeos.exception.EosApiException: Internal Service Error: eosio_assert_message assertion failure, detail: assertion failure with message: to account does not exist, detail: pending console output: