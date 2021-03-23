package com.linkFlow.manager.common.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.linkFlow.manager.common.model.define.OperatorState;
import com.linkFlow.manager.common.model.define.OperatorType;
import com.test32.common.converter.CustomDateTimeSerializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OperatorVO
{
    private Integer opIdx;

    private String opId;
    private String opName;
    private String opPwd;

    // certify, optional
    private String opCertNum;
    private String opCountryCode;
    private String opPhone;
    private String opEmail;

    private String opPrivateKey;


    private OperatorType opType;
    private Integer opLevel;
    private OperatorState opState;

    private BigDecimal opPoint;
    private Integer opGrade;

    private String opQrData;
    private String opQrUrl;


    // optional data
    private String opReferral;
    private String opPushToken;
    private String opPushState;
    private String opBirth;
    private String opGender;
    private String opLastTransactionId;


    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date opCreateDate;

    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date opUpdateDate;

    private Integer opParentIdx;

    private String opSymbol;/**이용할수 있는 심볼 리스트. 쉼표로 구분*/


    // tree view
    private String treeItem;
    private Integer treeLevel;
    private String treeRnum;
}