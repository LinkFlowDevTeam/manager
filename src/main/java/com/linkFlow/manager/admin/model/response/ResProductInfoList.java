package com.linkFlow.manager.admin.model.response;

import com.linkFlow.manager.common.model.BaseResponse;
import com.linkFlow.manager.common.model.vo.ProductInfoVO;
import lombok.Data;

import java.util.List;

@Data
public class ResProductInfoList extends BaseResponse
{
    List<ProductInfoVO> productInfoList;
}