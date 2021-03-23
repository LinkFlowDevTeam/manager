package com.linkFlow.manager.api.model.response;

import com.linkFlow.manager.common.model.BaseResponse;
import com.linkFlow.manager.common.model.vo.SysDataVOForApi;
import lombok.Data;

@Data
public class ResSystemInfo extends BaseResponse
{
	SysDataVOForApi data;
}