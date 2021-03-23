package com.linkFlow.manager.common.model.vo;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;

@Data
@Alias("TokenPointVOForSum")
public class TokenPointVOForSum
{
    private Integer tpTkIdx;
    private String tpTkSymbol;
    private BigDecimal tpPoint;
}