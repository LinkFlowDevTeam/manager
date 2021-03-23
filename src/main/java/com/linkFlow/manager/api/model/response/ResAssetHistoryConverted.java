package com.linkFlow.manager.api.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.linkFlow.manager.common.model.BaseResponse;
import lombok.Data;

@Data
public class ResAssetHistoryConverted extends BaseResponse
{
	@JsonProperty("DATE")
	String DATE;
	@JsonProperty("lfUSDT")
	String lfUSDT;
	@JsonProperty("lfUSDC")
	String lfUSDC;
}

