package com.linkFlow.manager.common.util.eos.json2bin;

import com.linkFlow.manager.common.util.eos.LocalApi_alliancex;
import com.linkFlow.manager.common.util.eos.Packer_alliancex;
import com.test32.common.wrapper.jafka.jeos.CommonEosApiUtil;
import com.test32.common.wrapper.jafka.jeos.core.common.SignArg;
import com.test32.common.wrapper.jafka.jeos.core.request.chain.transaction.PushTransactionRequest;

public class LocalApiImpl_alliancex implements LocalApi_alliancex
{
    @Override
    public PushTransactionRequest transfer(SignArg arg, String privateKey, String account, String from, String to, String quantity, String memo)
    {
        return CommonEosApiUtil.transfer(arg, privateKey, account, from, to, quantity, memo);
    }

    @Override
    public PushTransactionRequest lock(SignArg arg, String privateKey, String eosAccount, String actionName, AlliancexLock actionArg)
    {
        String packed = Packer_alliancex.packLock(actionArg);
        return CommonEosApiUtil.processCommon(arg, privateKey, eosAccount, actionName, eosAccount, packed);
    }

    @Override
    public PushTransactionRequest unlock(SignArg arg, String privateKey, String eosAccount, String actionName, AlliancexUnlock actionArg)
    {
        String packed = Packer_alliancex.packUnlock(actionArg);
        return CommonEosApiUtil.processCommon(arg, privateKey, eosAccount, actionName, eosAccount, packed);
    }

    @Override
    public PushTransactionRequest buyRam(SignArg arg, String privateKey, String payer, String receiver, String quantity)
    {
        return null;
    }

    @Override
    public PushTransactionRequest buyRamBytes(SignArg arg, String privateKey, String payer, String receiver, int bytes)
    {
        return null;
    }

    @Override
    public PushTransactionRequest delegatebw(SignArg arg, String privateKey, String from, String receiver, String stakeNetQuantity, String stakeCpuQuantity, Long transfer)
    {
        return null;
    }
}