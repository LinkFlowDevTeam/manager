package com.linkFlow.manager.api.model.response;

import com.linkFlow.manager.common.model.BaseResponse;
import com.linkFlow.manager.common.model.ReturnCode;
import com.linkFlow.manager.common.model.vo.ProductUserVOForApi;
import lombok.Data;

import java.util.List;

@Data
public class ResUserProduct extends BaseResponse
{
	List<ProductUserVOForApi> list;

	public ResUserProduct(ReturnCode returnCode) {
		super(returnCode);
	}
}