package com.linkFlow.manager.common.util.eos;

import com.linkFlow.manager.common.util.eos.json2bin.AlliancexLock;
import com.linkFlow.manager.common.util.eos.json2bin.AlliancexUnlock;
import com.test32.common.wrapper.jafka.jeos.LocalApi;
import com.test32.common.wrapper.jafka.jeos.core.common.SignArg;
import com.test32.common.wrapper.jafka.jeos.core.request.chain.transaction.PushTransactionRequest;

public interface LocalApi_alliancex extends LocalApi
{
    PushTransactionRequest lock(SignArg arg, String privateKey, String eosAccount, String actionName, AlliancexLock actionArg);
    PushTransactionRequest unlock(SignArg arg, String privateKey, String eosAccount, String actionName, AlliancexUnlock actionArg);
}