package com.test32.common.util;

import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;

import java.util.List;

public class MessageMaker
{
	public static String makeDescriptionMessage(String returnMessage, String detailMessage)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(returnMessage);

		if (!StringUtils.isEmpty(detailMessage))
		{
			sb.append(" - ");
			sb.append(detailMessage);
		}

		return sb.toString();
	}

	public static String makeFieldErrorsMessage(List<FieldError> fieldErrors)
	{
		StringBuilder sb = new StringBuilder();

		for (FieldError fieldError : fieldErrors)
		{
			if (sb.length() != 0)
			{
				sb.append(", ");
			}

			String message = fieldError.getDefaultMessage();

			if (message.contains("java.util.Date"))
			{
				message = "Check Date Format";
			}
			else if (message.contains("NumberFormatException"))
			{
				message = "Number Only";
			}

			sb.append(fieldError.getField());
			sb.append("(").append(message).append(")");
		}

		return sb.toString();
	}
}