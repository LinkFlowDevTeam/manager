package com.linkFlow.manager.admin;

import com.linkFlow.manager.common.model.ReturnCode;
import org.springframework.util.StringUtils;

public class ServiceException extends RuntimeException
{
	private final ReturnCode returnCode;
	private final String extraMessage;


	public ReturnCode getReturnCode()
	{
		return returnCode;
	}

	public ServiceException(ReturnCode returnCode)
	{
		this.returnCode = returnCode;
		this.extraMessage = "";
	}

	public ServiceException(ReturnCode returnCode, Exception e)
	{
		super(e);
		this.returnCode = returnCode;
		this.extraMessage = "";
	}

	public ServiceException(ReturnCode returnCode, String message)
	{
		super(message);
		this.returnCode = returnCode;
		this.extraMessage = "";
	}

	public ServiceException(ReturnCode returnCode, String message, String extraMessage)
	{
		super(message);
		this.returnCode = returnCode;
		this.extraMessage = extraMessage;
	}


	@Override
	public String getMessage()
	{
		String msg = super.getMessage();
		if (StringUtils.isEmpty(msg))
			msg = this.returnCode.getMessage();
		else
			msg = msg + " - " + this.returnCode.getMessage();

		return msg;
	}

	public String getExtraMessage(){
		return this.extraMessage;
	}

	public ServiceException(ReturnCode returnCode, String message, Exception e)
	{
		super(message, e);
		this.returnCode = returnCode;
		this.extraMessage = "";
	}

	public ServiceException(ReturnCode returnCode, String message, String extraMessage, Exception e)
	{
		super(message, e);
		this.returnCode = returnCode;
		this.extraMessage = extraMessage;
	}
}