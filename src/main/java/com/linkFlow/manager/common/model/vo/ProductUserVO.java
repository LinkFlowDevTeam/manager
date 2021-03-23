package com.linkFlow.manager.common.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.test32.common.converter.CustomDateTimeSerializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ProductUserVO
{
    private Integer puIdx;

    private Integer puPdIdx;
    private Integer puTkIdx;

    private Long puMbIdx;

    private Integer puState;

    @JsonSerialize(using = CustomDateTimeSerializer.class) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") private Date puCreateDate;
    @JsonSerialize(using = CustomDateTimeSerializer.class) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") private Date puUpdateDate;
    @JsonSerialize(using = CustomDateTimeSerializer.class) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") private Date puExpireDate;

    private BigDecimal puBaseAmount;
    private BigDecimal puInterestAmount;
    private BigDecimal puReturnAmount;
    private Integer puRewardTkIdx;
    private String puRewardRate;
    private String puRewardSymbol;
    private Integer puJoinDay;


    // join
    private ProductInfoVO productInfoVO;
    private MemberVO memberVO;
    private TokenInfoVO tokenInfoVO;
}