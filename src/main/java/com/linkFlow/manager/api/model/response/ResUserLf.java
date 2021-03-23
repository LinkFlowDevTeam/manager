package com.linkFlow.manager.api.model.response;

import com.linkFlow.manager.common.model.BaseResponse;
import com.linkFlow.manager.common.model.ReturnCode;
import com.linkFlow.manager.common.model.vo.LfUserVOForApi;
import lombok.Data;

import java.util.List;

@Data
public class ResUserLf extends BaseResponse
{
	List<LfUserVOForApi> list;

	public ResUserLf(ReturnCode returnCode) {
		super(returnCode);
	}
}