package com.linkFlow.manager.api.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class ReqUserAddressOnly
{
    @NotNull
    @Pattern(regexp="^[a-zA-Z0-9_]{42}$", message = "영문과 숫자만 사용 가능하고 길이제한은 4~16자 입니다")
    @Schema(description = "erc20 address", example = "ERC20 주소")
    private String address;
}