package com.linkFlow.manager.admin.model.response;

import com.linkFlow.manager.common.model.BaseResponse;
import lombok.Data;

@Data
public class ResProductCount extends BaseResponse
{
    private Long countActive;
    private Long countTotal;
}