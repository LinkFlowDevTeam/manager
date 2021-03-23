package com.linkFlow.manager.api.model.response;

import com.linkFlow.manager.common.model.BaseResponse;
import com.linkFlow.manager.common.model.vo.TokenPriceVOForApi;
import lombok.Data;

import java.util.List;

@Data
public class ResTokenPrice extends BaseResponse
{
	List<TokenPriceVOForApi> data;
}