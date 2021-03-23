package com.linkFlow.manager.admin.model.response;

import com.linkFlow.manager.common.model.BaseResponse;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ResDecimal extends BaseResponse
{
    private BigDecimal amount;
}