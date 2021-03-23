package com.linkFlow.manager.api.web;

import com.linkFlow.manager.admin.ServiceException;
import com.linkFlow.manager.common.model.BaseResponse;
import com.linkFlow.manager.common.model.ReturnCode;
import com.test32.common.util.MessageMaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice("com.linkFlow.manager.api")
@Order(value = 98)
public class ApiExceptionHandler
{
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@ExceptionHandler(BindException.class)
	@ResponseBody
	public BaseResponse bindExceptionHandle(BindException e)
	{
		BaseResponse response = new BaseResponse();
		ReturnCode errorCode = ReturnCode.FIELD_VALIDATE_FAIL;
		String description = MessageMaker.makeDescriptionMessage(errorCode.getMessage(), MessageMaker.makeFieldErrorsMessage(e.getFieldErrors()));

		response.setReturnCode(errorCode);
		response.setDescription(description);
		return response;
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseBody
	public BaseResponse handleMyException(Exception exception, HttpServletRequest request)
	{
		BaseResponse response = new BaseResponse();
		ReturnCode errorCode = ReturnCode.REQUIRED_FIELDS_MISSING;
		String description = errorCode.getMessage();

		response.setReturnCode(errorCode);
		response.setDescription(description);
		return response;
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	@ResponseBody
	public BaseResponse handle(Exception ex, HttpServletRequest request)
	{
		return processBadRequest();
	}

	private BaseResponse processBadRequest()
	{
		BaseResponse response = new BaseResponse();
		ReturnCode errorCode = ReturnCode.BAD_REQUEST;
		String description = errorCode.getMessage();

		response.setReturnCode(errorCode);
		response.setDescription(description);
		return response;
	}

	@ExceptionHandler(ServiceException.class)
	@ResponseBody
	public Object serviceExceptionHandler(ServiceException e)
	{
		if(e.getReturnCode() == ReturnCode.TICKET_EXPIRED || e.getReturnCode() == ReturnCode.TICKET_NOT_EXIST)
			logger.info(e.toString());
		else
			logger.error(e.toString(), e);

		BaseResponse result = new BaseResponse();
		String description = MessageMaker.makeDescriptionMessage(e.getReturnCode().getMessage(), e.getMessage());
		result.setReturnCode(e.getReturnCode());
		result.setDescription(description);
		result.setExtraMessage(e.getExtraMessage());

		return result;
	}

	@ExceptionHandler(BadSqlGrammarException.class)
	@ResponseBody
	public Object badGrammarExceptionHandler(BadSqlGrammarException e)
	{
		logger.error(e.toString(), e);

		BaseResponse result = new BaseResponse();
		result.setReturnCode(ReturnCode.INTERNAL_ERROR);
		result.setDescription(result.getReturnCode().getMessage());
		return result;
	}

	@ExceptionHandler(Exception.class)
	@ResponseBody
	public Object exceptionHandle(Exception e)
	{
		logger.error(e.toString(), e);

		BaseResponse result = new BaseResponse();
		ReturnCode returnCode = ReturnCode.INTERNAL_ERROR;
		String description = MessageMaker.makeDescriptionMessage(returnCode.getMessage(), e.toString());
		result.setReturnCode(returnCode);
		result.setDescription(description);
		return result;
	}
}