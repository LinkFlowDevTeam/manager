package com.test32.common.model.blockChain.etherscan;

import lombok.Data;

@Data
public class ResEtherScanEthPriceRoot
{
	String status;
	String message;
	ResEtherScanEthPriceData result;
}