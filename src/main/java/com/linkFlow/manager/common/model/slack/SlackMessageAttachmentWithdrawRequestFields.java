package com.linkFlow.manager.common.model.slack;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.test32.common.converter.CustomDateTimeSerializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class SlackMessageAttachmentWithdrawRequestFields
{
    @JsonProperty("요청일")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date;

    @JsonProperty("타입")
    private String type;
    @JsonProperty("아이디")
    private String userId;
    @JsonProperty("이름")
    private String userName;

    @JsonProperty("출금량")
    private String quantity;
    @JsonProperty("관리자계정")
    private String from;

    @JsonProperty("출금주소")
    private String to;
    @JsonProperty("메모")
    private String memo;
}
//출금 요청,,출금 승인 필요,15,,VII,10,kiru123kiru,aa