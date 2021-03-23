package com.linkFlow.manager.api.model.response;

import com.linkFlow.manager.common.model.BaseResponse;
import com.linkFlow.manager.common.model.ReturnCode;
import com.linkFlow.manager.common.model.vo.TokenPointVOForApi;
import lombok.Data;

import java.util.List;

@Data
public class ResUserToken extends BaseResponse
{
	List<TokenPointVOForApi> list;

	public ResUserToken(ReturnCode returnCode) {
		super(returnCode);
	}
}