package com.linkFlow.manager.common.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.test32.common.converter.CustomDateTimeSerializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class ApiCurrencyRaw
{
	private Long curIdx;
	@JsonSerialize(using = CustomDateTimeSerializer.class)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date curUpdateDate;
	private String curSearchDate;

	private Integer result;

	private String cur_unit;
	private String cur_nm;

	private String ttb;
	private String tts;
	private String deal_bas_r;
	private String bkpr;
	private String kftc_bkpr;
	private String kftc_deal_bas_r;
}