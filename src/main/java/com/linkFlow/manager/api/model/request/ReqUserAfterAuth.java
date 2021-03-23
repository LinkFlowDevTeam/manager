package com.linkFlow.manager.api.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ReqUserAfterAuth
{
    //@Pattern(regexp = "[a-z]{3}")
    //@Size(min = 1, max = 3)
    //@Pattern(regexp = "[0-9]{10,11}", message = "10~11자리의 숫자만 입력가능합니다")

    @NotNull
    @Schema(example = "idE05EF18F7A4B909207F38E9CF8B8D1")
    private String id;

    @NotNull
    @Schema(example = "sign1AE4A8A8E673574446CD82E4B6BA")
    private String ticketId;
}