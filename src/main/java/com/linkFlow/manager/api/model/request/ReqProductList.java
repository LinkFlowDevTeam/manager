package com.linkFlow.manager.api.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ReqProductList
{
    @Schema(example = "로그인 하면 전송")
    private String address;

    @Schema(example = "출력언어, 일단 보류")
    private String lang;
}