package com.linkFlow.manager.common.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.test32.common.converter.CustomDateTimeSerializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ProductInfoVO
{
    private Integer pdIdx;
    private Integer pdOrder;

    private Integer pdTkIdx;
    private String pdTkSymbol;
    private String pdType;
    private String pdName;
    private String pdDetail;

    private Integer pdState;
    private Integer pdBlockState;

    @JsonSerialize(using = CustomDateTimeSerializer.class) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") private Date pdCreateDate;
    @JsonSerialize(using = CustomDateTimeSerializer.class) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") private Date pdUpdateDate;
    @JsonSerialize(using = CustomDateTimeSerializer.class) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") private Date pdStartDate;
    @JsonSerialize(using = CustomDateTimeSerializer.class) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") private Date pdExpireDate;

    @JsonSerialize(using = CustomDateTimeSerializer.class) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") private Date pdInterestDate;



    private Integer pdDay;
    private Long pdMinAmount;
    private Long pdLimitAmount;

    private BigDecimal pdBaseAmount;
    private BigDecimal pdCurrentInterest;
    private String pdInterestRate;

    private String pdErc20Address;
    private BigDecimal pdEthPoint;
    private BigDecimal pdErc20Point;
    private BigDecimal pdErc20Cached;
    @JsonSerialize(using = CustomDateTimeSerializer.class) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") private Date pdErc20CachedDate;
    private Integer pdErc20State;
    private String pdErc20Key;



    // join
    private TokenInfoVO tokenInfoVO;

}