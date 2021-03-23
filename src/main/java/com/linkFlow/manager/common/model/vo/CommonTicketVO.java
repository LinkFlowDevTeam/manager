package com.linkFlow.manager.common.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.test32.common.converter.CustomDateTimeSerializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class CommonTicketVO
{
	private Long tkIdx;
	private Integer tkState;

	private String tkRequestData;
	private String tkUserId;
	private String tkSign;

	private Integer tkErrorCount;

	@JsonSerialize(using = CustomDateTimeSerializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date tkCreateDate;

	@JsonSerialize(using = CustomDateTimeSerializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date tkUpdateDate;
}