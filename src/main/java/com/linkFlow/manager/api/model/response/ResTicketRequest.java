package com.linkFlow.manager.api.model.response;

import com.linkFlow.manager.common.model.BaseResponse;
import com.linkFlow.manager.common.model.ReturnCode;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResTicketRequest extends BaseResponse
{
	private String id;
	private String ticketId;

	public ResTicketRequest(ReturnCode returnCode) {
		super(returnCode);
	}
}