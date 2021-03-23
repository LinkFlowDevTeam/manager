package com.linkFlow.manager.api.model.response;

import com.linkFlow.manager.common.model.BaseResponse;
import com.linkFlow.manager.common.model.vo.MemberTierVOForApi;
import lombok.Data;

import java.util.List;

@Data
public class ResTier extends BaseResponse
{
	List<MemberTierVOForApi> data;
}