package com.test32.common.wrapper.jafka.jeos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test32.common.wrapper.jafka.jeos.core.common.SignArg;
import com.test32.common.wrapper.jafka.jeos.core.request.chain.transaction.PushTransactionRequest;
import com.test32.common.wrapper.jafka.jeos.impl.EosApiServiceGenerator;

public interface LocalApi
{
    default ObjectMapper getObjectMapper() { return EosApiServiceGenerator.getMapper(); }
    default PushTransactionRequest transfer(SignArg arg, String privateKey, String from, String to, String quantity, String memo) { return transfer(arg, privateKey, "eosio.token", from, to, quantity, memo); }
    PushTransactionRequest transfer(SignArg arg, String privateKey, String account, String from, String to, String quantity, String memo);
    PushTransactionRequest buyRam(SignArg arg, String privateKey, String payer, String receiver, String quantity);
    PushTransactionRequest buyRamBytes(SignArg arg, String privateKey, String payer, String receiver, int bytes);
    PushTransactionRequest delegatebw(SignArg arg, String privateKey, String from, String receiver, String stakeNetQuantity, String stakeCpuQuantity, Long transfer);
}