package com.linkFlow.manager.api.model.request;

import lombok.Data;

@Data
public class ReqCommonPaging
{
    //@NotNull
    private Integer pageNo;

    //@NotNull
    private Integer pageSize;

    // 언어 체크용
    private String lang;
}