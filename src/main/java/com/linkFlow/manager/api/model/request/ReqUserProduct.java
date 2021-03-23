package com.linkFlow.manager.api.model.request;

import lombok.Data;

@Data
public class ReqUserProduct extends ReqUserAddressOnly
{
    private Integer productId;
}