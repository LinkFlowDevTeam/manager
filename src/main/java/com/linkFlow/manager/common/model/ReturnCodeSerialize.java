package com.linkFlow.manager.common.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class ReturnCodeSerialize extends JsonSerializer<ReturnCode>
{
	@Override
	public void serialize(ReturnCode walletReturnCode, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException
	{
		jsonGenerator.writeNumber(walletReturnCode.getCode());
	}
}