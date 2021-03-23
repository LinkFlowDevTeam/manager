package com.linkFlow.manager.api.model.response;

import com.linkFlow.manager.common.model.BaseResponse;
import com.linkFlow.manager.common.model.ReturnCode;
import com.linkFlow.manager.common.model.vo.MemberVOForApi;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResUserInfo extends BaseResponse
{
	MemberVOForApi data;

	

	public ResUserInfo(ReturnCode returnCode) {
		super(returnCode);
	}
}