package com.test32.common.model;

import lombok.Data;

@Data
public class CustomHttpResponse
{
	private Integer responseCode;
	private String baseResponse;

	private String requestUrl;
	private String errorMessage;
}