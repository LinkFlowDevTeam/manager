package com.linkFlow.manager.common.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.test32.common.converter.CustomDateTimeSerializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ProductInfoVOForApi
{
    private Integer productId;
    private Integer order;


    private String symbol;
    private String type;
    private String name;
    private String detail;

    private Integer state;




    @JsonSerialize(using = CustomDateTimeSerializer.class) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") private Date startDate;
    @JsonSerialize(using = CustomDateTimeSerializer.class) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") private Date expireDate;





    private Integer day;
    private Long minAmount;
    private Long limitAmount;

    private BigDecimal baseAmount;
    private BigDecimal currentInterest;
    private String interestRate;

    private String erc20Address;



    // join
    //private BigDecimal myDepositAmount;
}