package com.linkFlow.manager.api.model.request;

import lombok.Data;

@Data
public class BaseRequest
{
    private String hash;
    private String operatorId;
    private Long time;
}