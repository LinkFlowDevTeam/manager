package com.linkFlow.manager.api.model.response;

import com.linkFlow.manager.common.model.BaseResponse;
import com.linkFlow.manager.common.model.ReturnCode;
import com.linkFlow.manager.common.model.vo.ProductInfoVOForApi;
import lombok.Data;

import java.util.List;

@Data
public class ResProductList extends BaseResponse
{
	List<ProductInfoVOForApi> data;

	public ResProductList(ReturnCode returnCode) {
		super(returnCode);
	}
}