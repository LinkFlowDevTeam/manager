package com.linkFlow.manager.api.model.response;

import com.linkFlow.manager.common.model.BaseResponse;
import com.linkFlow.manager.common.model.vo.TransactionVOForApi;
import lombok.Data;

@Data
public class ResUserTransactionDetail extends BaseResponse
{
	TransactionVOForApi data;
}