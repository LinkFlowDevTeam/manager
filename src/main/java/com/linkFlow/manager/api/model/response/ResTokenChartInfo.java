package com.linkFlow.manager.api.model.response;

import com.linkFlow.manager.common.model.BaseResponse;
import com.linkFlow.manager.common.model.vo.TokenChartVOForApi;
import lombok.Data;

import java.util.List;

@Data
public class ResTokenChartInfo extends BaseResponse
{
	List<TokenChartVOForApi> data;
}