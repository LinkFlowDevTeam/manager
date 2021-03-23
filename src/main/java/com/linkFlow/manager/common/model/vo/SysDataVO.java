package com.linkFlow.manager.common.model.vo;

import lombok.Data;

@Data
public class SysDataVO
{
    private Integer ssIdx;
    private Integer ssErc20GasLimit;
    private Integer ssErc20GasPricePercent;
    private Integer ssErc20PendingNoticeM;

    private Integer ssLfTokenLockDay;
    private String ssLfTokenDepositAddress;
    private String ssProductClaimFee;

    private String ssProductPurchaseWithdrawAddress;
    private String ssGasFeeAddress;
}