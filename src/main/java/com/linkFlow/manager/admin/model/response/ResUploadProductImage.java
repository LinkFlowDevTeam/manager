package com.linkFlow.manager.admin.model.response;

import com.linkFlow.manager.common.model.BaseResponse;
import lombok.Data;

@Data
public class ResUploadProductImage extends BaseResponse
{
	String fileName;
	String filePath;
}