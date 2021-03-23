package com.linkFlow.manager.common.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.test32.common.converter.CustomDateTimeSerializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TokenPointVO
{
    private Long tpIdx;
    private Integer tpTkIdx;
    private String tpTkSymbol;
    //private String tpTkType;
    private Long tpMbIdx;
    private BigDecimal tpPoint;
    private BigDecimal tpLockedPoint;

    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date tpCreateDate;

    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date tpUpdateDate;



    // join
    private MemberVO memberVO;
    private TokenInfoVO tokenInfoVO;
}