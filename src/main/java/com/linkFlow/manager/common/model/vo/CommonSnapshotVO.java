package com.linkFlow.manager.common.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.test32.common.converter.CustomDateTimeSerializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class CommonSnapshotVO
{
    private Long csTargetIdx;
    private Integer csType;
    private Integer csState;

    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date csUpdateDate;

    private String csData1;
    private String csData2;
    private String csData3;
    private String csData4;
    private String csData5;
    private String csData6;
    private String csData7;
}