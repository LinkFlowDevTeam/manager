package com.linkFlow.manager.api.model.response;

import com.linkFlow.manager.common.model.BaseResponse;
import com.linkFlow.manager.common.model.vo.DailyProductSummaryVOForApi;
import lombok.Data;

import java.util.List;

@Data
public class ResAssetHistory extends BaseResponse
{
	List<DailyProductSummaryVOForApi> data;
}