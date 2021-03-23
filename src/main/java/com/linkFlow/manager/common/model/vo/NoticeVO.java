package com.linkFlow.manager.common.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.test32.common.converter.CustomDateTimeSerializer;
import lombok.Data;
import org.apache.ibatis.type.Alias;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@Alias("NoticeVO")
public class NoticeVO
{
	private Long ntIdx;
    private Integer ntState;

    private String ntTitle;
    private String ntSubTitle;
    private String ntMessage;
    private Integer ntOrder;


    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date ntCreateDate;

    private String createDateString;
}