package com.test32.common.wrapper.jafka.jeos.impl;

import com.test32.common.wrapper.jafka.jeos.CommonEosApiUtil;
import com.test32.common.wrapper.jafka.jeos.LocalApi;
import com.test32.common.wrapper.jafka.jeos.core.common.SignArg;
import com.test32.common.wrapper.jafka.jeos.core.request.chain.transaction.PushTransactionRequest;

public class LocalApiImpl implements LocalApi
{
    @Override
    public PushTransactionRequest transfer(SignArg arg, String privateKey, String account, String from, String to, String quantity, String memo)
    {
        return CommonEosApiUtil.transfer(arg, privateKey, account, from, to, quantity, memo);
    }

    @Override
    public PushTransactionRequest buyRam(SignArg arg, String privateKey, String payer, String receiver, String quantity)
    {
        return CommonEosApiUtil.buyRam(arg, privateKey, payer, receiver, quantity);
    }

    @Override
    public PushTransactionRequest buyRamBytes(SignArg arg, String privateKey, String payer, String receiver, int bytes)
    {
        return CommonEosApiUtil.buyRamBytes(arg, privateKey, payer, receiver, bytes);
    }

    @Override
    public PushTransactionRequest delegatebw(SignArg arg, String privateKey, String from, String receiver, String stakeNetQuantity, String stakeCpuQuantity, Long transfer)
    {
        return CommonEosApiUtil.delegatebw(arg, privateKey, from, receiver, stakeNetQuantity, stakeCpuQuantity, transfer);
    }


}