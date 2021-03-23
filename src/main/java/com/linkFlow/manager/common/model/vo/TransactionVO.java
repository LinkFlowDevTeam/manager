package com.linkFlow.manager.common.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.test32.common.converter.CustomDateTimeSerializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TransactionVO
{
    private Long trIdx;
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date trCreateDate;

    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date trUpdateDate;

    private String trYear;
    private String trMonth;
    private String trDate;

    private Long trMbIdx;
    private String trMbId;

    // unique with type
    private Integer trType;
    private String trUniqueId;

    private Integer trState;
    private Integer trUpdateType;

    private Integer trTkIdx;
    private String trTkSymbol;

    private BigDecimal trAmount;
    private BigDecimal trPointBefore;
    private BigDecimal trPointAfter;


    // for copy to
    private MemberVO memberVO;
    private TrExtraVO trExtraVO;
}