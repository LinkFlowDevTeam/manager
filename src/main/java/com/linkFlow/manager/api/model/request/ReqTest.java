package com.linkFlow.manager.api.model.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class ReqTest
{
    //@Pattern(regexp = "[a-z]{3}")
    //@Size(min = 1, max = 3)
    //@Pattern(regexp = "[0-9]{10,11}", message = "10~11자리의 숫자만 입력가능합니다")

    @NotNull
    @Email
    private String email;

    @NotNull
    @Pattern(regexp = "[0-9]{1,3}", message = "1~3자리의 숫자만 입력가능합니다")
    private String countryCode;

    @NotNull
    @Pattern(regexp = "[0-9]{8,20}", message = "8~20자리의 숫자만 입력가능합니다")
    private String phone;

    @Pattern(regexp = "[fmFM]")
    private String gender;

    @Pattern(regexp = "[01]")
    private String pushState;
}