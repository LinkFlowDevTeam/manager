package com.test32.common.model.blockChain.erc20;

import com.linkFlow.manager.common.model.BaseResponse;
import lombok.Data;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

@Data
public class Erc20SendEtherResponse extends BaseResponse
{
	private TransactionReceipt transactionReceipt;
}