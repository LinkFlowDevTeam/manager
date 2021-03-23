package com.linkFlow.manager.api.model.response;

import com.linkFlow.manager.common.model.BaseResponse;
import lombok.Data;

@Data
public class ResUploadImage extends BaseResponse
{
	String fileName;
	String filePath;
}