package com.linkFlow.manager.api.model.response;

import com.linkFlow.manager.common.model.BaseResponse;
import com.linkFlow.manager.common.model.vo.TokenInfoVOForApi;
import lombok.Data;

import java.util.List;

@Data
public class ResTokenInfo extends BaseResponse
{
	List<TokenInfoVOForApi> data;
}