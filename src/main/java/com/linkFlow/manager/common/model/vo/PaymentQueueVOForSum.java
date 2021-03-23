package com.linkFlow.manager.common.model.vo;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;

@Data
@Alias("PaymentQueueVOForSum")
public class PaymentQueueVOForSum
{
    // unique
    private Integer pqPaymentType;

    private Integer countItem;
    private BigDecimal pqAmount;
    private BigDecimal pqReqAmount;
    private BigDecimal pqFee;
}
