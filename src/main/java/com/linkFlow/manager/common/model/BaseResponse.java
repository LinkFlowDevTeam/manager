package com.linkFlow.manager.common.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain=true)
public class BaseResponse
{
	@JsonSerialize(using = ReturnCodeSerialize.class)
	private ReturnCode returnCode = ReturnCode.UNKNOWN_ERROR;
	private String description = ReturnCode.UNKNOWN_ERROR.getMessage();
	private String extraMessage;

	public BaseResponse(ReturnCode returnCode){
		this.returnCode = returnCode;
		this.description = returnCode.getMessage();
	}

	public BaseResponse setReturnCodeAndDescription(ReturnCode returnCode){
		this.returnCode = returnCode;
		this.description = returnCode.getMessage();

		return this;
	}
}