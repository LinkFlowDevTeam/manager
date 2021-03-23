package com.linkFlow.manager.common.util.eos;


import com.linkFlow.manager.common.util.eos.json2bin.AlliancexLock;
import com.linkFlow.manager.common.util.eos.json2bin.AlliancexUnlock;
import com.test32.common.wrapper.jafka.jeos.CommonEosApiUtil;
import com.test32.common.wrapper.jafka.jeos.EosApi;
import com.test32.common.wrapper.jafka.jeos.EosApiFactory;
import com.test32.common.wrapper.jafka.jeos.LocalApi;
import com.test32.common.wrapper.jafka.jeos.convert.Packer;
import com.test32.common.wrapper.jafka.jeos.core.common.SignArg;
import com.test32.common.wrapper.jafka.jeos.core.common.transaction.PackedTransaction;
import com.test32.common.wrapper.jafka.jeos.core.common.transaction.TransactionAction;
import com.test32.common.wrapper.jafka.jeos.core.common.transaction.TransactionAuthorization;
import com.test32.common.wrapper.jafka.jeos.core.request.chain.json2bin.TransferArg;
import com.test32.common.wrapper.jafka.jeos.core.request.chain.transaction.PushTransactionRequest;
import com.test32.common.wrapper.jafka.jeos.core.response.chain.Block;
import com.test32.common.wrapper.jafka.jeos.core.response.chain.transaction.PushedTransaction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomEosUtil
{
    public static PushedTransaction transfer(String endpoint, String privateKey, String eosAccount, String from, String to, String quantity, String memo)
    {
        EosApi eosApi = EosApiFactory.create(endpoint);
        SignArg arg = eosApi.getSignArg(120);
        LocalApi localApi = EosApiFactory.createLocalApi();

        PushTransactionRequest req = localApi.transfer(arg, privateKey, eosAccount, from, to, quantity, memo);
        return eosApi.pushTransaction(req);
    }

    public static PushedTransaction lock(String endpoint, String privateKey, String eosAccount, String user, String quantity)
    {
        EosApi eosApi = EosApiFactory.create(endpoint);
        SignArg arg = eosApi.getSignArg(120);
        LocalApi_alliancex localApi = EosApiFactory_alliancex.createLocalApi();

        AlliancexLock actionArg = new AlliancexLock();
        actionArg.setUser(user);
        actionArg.setQuantity(quantity);
        PushTransactionRequest req = localApi.lock(arg, privateKey, eosAccount, "lock", actionArg);
        return eosApi.pushTransaction(req);
    }

    public static PushedTransaction unlock(String endpoint, String privateKey, String eosAccount, String user, String quantity)
    {
        EosApi eosApi = EosApiFactory.create(endpoint);
        SignArg arg = eosApi.getSignArg(120);
        LocalApi_alliancex localApi = EosApiFactory_alliancex.createLocalApi();

        AlliancexUnlock actionArg = new AlliancexUnlock();
        actionArg.setUser(user);
        actionArg.setQuantity(quantity);
        PushTransactionRequest req = localApi.unlock(arg, privateKey, eosAccount, "unlock", actionArg);
        return eosApi.pushTransaction(req);
    }

    public static PushedTransaction transferLock(String endpoint, String privateKey, String eosAccount, String user, BigDecimal amount, String symbol)
    {
        EosApi eosApi = EosApiFactory.create(endpoint);
        SignArg arg = eosApi.getSignArg(120);

        Block block = eosApi.getBlock(eosApi.getChainInfo().getHeadBlockId());
        System.out.println("blockNum=" + block.getBlockNum());

        List<TransactionAuthorization> authorizations = Arrays.asList(new TransactionAuthorization(eosAccount, "active"));


        List<TransactionAction> actions = new ArrayList<>();
        {
            String quantity = amount.setScale(4, RoundingMode.HALF_UP) + " " + symbol;
            TransferArg transferArg = new TransferArg(eosAccount, user, quantity, "");
            String packed = Packer.packTransfer(transferArg);
            TransactionAction tx = new TransactionAction(eosAccount, "transfer", authorizations, packed);
            actions.add(tx);
        }

        {
            String quantity = amount.setScale(4, RoundingMode.HALF_UP) + " " + symbol;
            AlliancexLock actionArg = new AlliancexLock();
            actionArg.setUser(user);
            actionArg.setQuantity(quantity);
            String actionName = "lock";
            String packed = Packer_alliancex.packLock(actionArg);
            TransactionAction tx = new TransactionAction(eosAccount, actionName, authorizations, packed);
            actions.add(tx);
        }

        // ⑦ build the packed transaction
        PackedTransaction packedTransaction = new PackedTransaction();
        packedTransaction.setRefBlockPrefix(block.getRefBlockPrefix());
        packedTransaction.setRefBlockNum(block.getBlockNum());
        // expired after 3 minutes
        String expiration = ZonedDateTime.now(ZoneId.of("GMT")).plusMinutes(3).truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        LocalDateTime expireTimeFormat = LocalDateTime.parse(expiration);

        packedTransaction.setExpiration(expireTimeFormat);
        packedTransaction.setRegion("0");
        packedTransaction.setMaxNetUsageWords(0);
        packedTransaction.setMaxCpuUsageMs(0);
        packedTransaction.setDelaySec(0);
        packedTransaction.setActions(actions);

        PushTransactionRequest req = CommonEosApiUtil.makePushTransaction(arg, privateKey, packedTransaction);
        return eosApi.pushTransaction(req);
    }
}