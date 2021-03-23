package com.test32.common.axisj;

public class AjaxResultMessage
{
	private boolean result;

	private String messageCode;

	private String message;

	private Object data;

	public AjaxResultMessage()
	{
		this.result = false;
	}

	public AjaxResultMessage(boolean result, String messageCode, String message, Object data)
	{
		super();
		this.result = result;
		this.messageCode = messageCode;
		this.message = message;
		this.data = data;
	}

	public boolean isResult()
	{
		return result;
	}

	public void setResult(boolean result)
	{
		this.result = result;
	}

	public String getMessageCode()
	{
		return messageCode;
	}

	public void setMessageCode(String messageCode)
	{
		this.messageCode = messageCode;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public Object getData()
	{
		return data;
	}

	public void setData(Object data)
	{
		this.data = data;
	}

	@Override
	public String toString()
	{
		return "AjaxResultMessage [result=" + result + ", messageCode=" + messageCode + ", message=" + message + ", data=" + data + "]";
	}
}