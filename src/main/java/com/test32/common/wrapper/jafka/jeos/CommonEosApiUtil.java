package com.test32.common.wrapper.jafka.jeos;

import com.test32.common.wrapper.jafka.jeos.convert.Packer;
import com.test32.common.wrapper.jafka.jeos.core.common.SignArg;
import com.test32.common.wrapper.jafka.jeos.core.common.transaction.PackedTransaction;
import com.test32.common.wrapper.jafka.jeos.core.common.transaction.TransactionAction;
import com.test32.common.wrapper.jafka.jeos.core.common.transaction.TransactionAuthorization;
import com.test32.common.wrapper.jafka.jeos.core.request.chain.json2bin.*;
import com.test32.common.wrapper.jafka.jeos.core.request.chain.transaction.PushTransactionRequest;
import com.test32.common.wrapper.jafka.jeos.core.response.chain.AbiJsonToBin;
import com.test32.common.wrapper.jafka.jeos.core.response.chain.Block;
import com.test32.common.wrapper.jafka.jeos.core.response.chain.TableRow;
import com.test32.common.wrapper.jafka.jeos.core.response.chain.account.Account;
import com.test32.common.wrapper.jafka.jeos.core.response.chain.account.Key;
import com.test32.common.wrapper.jafka.jeos.core.response.chain.account.RequiredAuth;
import com.test32.common.wrapper.jafka.jeos.core.response.chain.transaction.PushedTransaction;
import com.test32.common.wrapper.jafka.jeos.util.KeyUtil;
import com.test32.common.wrapper.jafka.jeos.util.Raw;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CommonEosApiUtil
{
    // https://github.com/adyliu/jeos
    public static String API_ENDPOINT_MAINNET = "https://proxy.eosnode.tools:443";
    public static String API_ENDPOINT_JUNGLE = "https://jungle2.cryptolions.io:443";
    public static String API_ENDPOINT_KYLIN = "http://kylin.meet.one:8888";

    public final static int BIG_DECIMAL_ROUNDING_MODE = BigDecimal.ROUND_HALF_UP;

    public static Account getAccount(String endpoint, String accountName)
    {
        EosApi eosApi = EosApiFactory.create(endpoint);
        return eosApi.getAccount(accountName);
    }

    public static BigDecimal getRamPrice(String endpoint)
    {
        // https://eosio.stackexchange.com/questions/847/how-to-get-current-last-ram-price
        // Connector Balance/(Smart Token’s Outstanding supply × Connector Weight
        // quote.balance / (base.balance * quote.weight)
        TableRow tableRow = CommonEosApiUtil.getTableRows( endpoint,  "eosio", "eosio", "rammarket", null, null, null);
        if (tableRow != null) {
            Map<String, ?> row = tableRow.getRows().get(0);
            Map<String, String> base = (Map<String, String>)row.get("base");
            Map<String, String> quote = (Map<String, String>)row.get("quote");

            BigDecimal baseBalance = new BigDecimal(base.get("balance").toLowerCase().split(" ")[0]).setScale(10, RoundingMode.HALF_UP);
            BigDecimal quoteBalance = new BigDecimal(quote.get("balance").toLowerCase().split(" ")[0]).setScale(10, RoundingMode.HALF_UP);
            BigDecimal quoteWeight = new BigDecimal(quote.get("weight").toLowerCase().split(" ")[0]).setScale(10, RoundingMode.HALF_UP);

            //return quoteBalance.divide( baseBalance.multiply(quoteWeight) , RoundingMode.HALF_UP).setScale(10, RoundingMode.HALF_UP);
            return quoteBalance.divide(baseBalance, 8, RoundingMode.HALF_UP).setScale(10, RoundingMode.HALF_UP);
        }
        return null;
    }

    public static BigDecimal getRamPrice(String endpoint, Integer iterationCount)
    {
        BigDecimal result = null;
        for(int i = 0; i < iterationCount; i++)
        {
            try
            {
                result = getRamPrice(endpoint);
                if(result != null)
                    break;
            }
            catch (Exception e)
            {
                System.out.println(e.toString());
            }
        }
        return result;
    }

    public static List<String> getEosCurrencyBalance(String endpoint, String accountName)
    {
        return getCurrencyBalance(endpoint, "eosio.token", accountName, "EOS");
    }

    public static List<String> getCurrencyBalance(String endpoint, String code, String accountName, String symbol)
    {
        EosApi eosApi = EosApiFactory.create(endpoint);
        return eosApi.getCurrencyBalance(code, accountName, symbol);
    }

    public static String getBalance(String endpoint, String scope, String code, String table, String userName)
    {
        EosApi eosApi = EosApiFactory.create(endpoint);
        TableRow tableRow = eosApi.getTableRowsByUserName(scope, code, table, userName);
        if(tableRow.getRows().size() == 1)
        {
            Map<String, ?> row = tableRow.getRows().get(0);
            return row.get("balance").toString();
        }
        return null;
    }

    public static TableRow getTableRows(String endpoint, String scope, String code, String table, String lowerBound, String upperBound, Integer limit)
    {
        try
        {
            EosApi eosApi = EosApiFactory.create(endpoint);
            return eosApi.getTableRows(scope, code, table, lowerBound, upperBound, limit);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static TableRow getTableRowsByKeyIndex(String endpoint, String scope, String code, String table, String keyType, Integer indexPosition, String lowerBound, String upperBound, Integer limit)
    {
        try
        {
            EosApi eosApi = EosApiFactory.create(endpoint);
            return eosApi.getTableRowsByKeyIndex(scope, code, table, keyType, indexPosition, lowerBound, upperBound, limit);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static String sign(String privateKey, SignArg arg, PackedTransaction t)
    {
        Raw raw = Packer.packPackedTransaction(arg.getChainId(), t);
        raw.pack(ByteBuffer.allocate(33).array());// TODO: what's this?
        return KeyUtil.signHash(privateKey, raw.bytes());
    }
    public static String createPrivateKey() {
        return KeyUtil.createPrivateKey();
    }

    public static String toPublicKey(String privateKey) {
        return KeyUtil.toPublicKey(privateKey);
    }

    public static PushTransactionRequest eosTransfer(SignArg arg, String privateKey, String from, String to, String quantity, String memo)
    {
        TransferArg transferArg = new TransferArg(from, to, quantity, memo);
        String transferData = Packer.packTransfer(transferArg);

        String actionName = "transfer";
        return processCommon(arg, privateKey, from, actionName, from, transferData);
    }


    public static PushTransactionRequest transfer(SignArg arg, String privateKey, String account, String from, String to, String quantity, String memo)
    {
        TransferArg transferArg = new TransferArg(from, to, quantity, memo);
        String transferData = Packer.packTransfer(transferArg);

        String actionName = "transfer";
        if(account.equalsIgnoreCase("eosio.token"))
            return processEosTransferCommon(arg, privateKey, from, actionName, transferData);
        else
            return processCommon(arg, privateKey, account, actionName, from, transferData);
    }

    public static PushTransactionRequest buyRam(SignArg arg, String privateKey, String payer, String receiver, String quantity)
    {
        BuyRamArg buyRamArg = new BuyRamArg(payer, receiver, quantity);
        String transferData = Packer.packBuyram(buyRamArg);

        String actionName = "buyram";
        List<TransactionAuthorization> authorizations = Arrays.asList(new TransactionAuthorization(payer, "active"));
        List<TransactionAction> actions = Arrays.asList(new TransactionAction("eosio", actionName, authorizations, transferData));

        PackedTransaction packedTransaction = makeCommonPart(arg);
        packedTransaction.setActions(actions);
        return makePushTransaction(arg, privateKey, packedTransaction);
    }

    public static PushTransactionRequest buyRamBytes(SignArg arg, String privateKey, String payer, String receiver, int bytes)
    {
        BuyRamByteArg buyRamByteArg = new BuyRamByteArg(payer, receiver, bytes);
        String transferData = Packer.packBuyrambytes(buyRamByteArg);

        String actionName = "buyrambytes";
        List<TransactionAuthorization> authorizations = Arrays.asList(new TransactionAuthorization(payer, "active"));
        List<TransactionAction> actions = Arrays.asList(new TransactionAction("eosio", actionName, authorizations, transferData));

        PackedTransaction packedTransaction = makeCommonPart(arg);
        packedTransaction.setActions(actions);
        return makePushTransaction(arg, privateKey, packedTransaction);
    }

    public static PushTransactionRequest delegatebw(SignArg arg, String privateKey, String from, String receiver, String stakeNetQuantity, String stakeCpuQuantity, Long transfer)
    {
        DelegatebwArg delegatebwArg = new DelegatebwArg(from, receiver, stakeNetQuantity, stakeCpuQuantity, transfer);
        String transferData = Packer.packDelegatebw(delegatebwArg);

        String actionName = "delegatebw";
        List<TransactionAuthorization> authorizations = Arrays.asList(new TransactionAuthorization(from, "active"));
        List<TransactionAction> actions = Arrays.asList(new TransactionAction("eosio", actionName, authorizations, transferData));

        PackedTransaction packedTransaction = makeCommonPart(arg);
        packedTransaction.setActions(actions);
        return makePushTransaction(arg, privateKey, packedTransaction);
    }


    private static CreateAccountArg getCreateAccountArg(String creator, String newAccountName, String newAccountPublicKey)
    {
        CreateAccountArg createAccountArg = new CreateAccountArg();

        createAccountArg.setCreator(creator);
        createAccountArg.setName(newAccountName);
        // set the owner key
        RequiredAuth owner = new RequiredAuth();
        owner.setThreshold(1L);
        owner.setAccounts(Collections.EMPTY_LIST);
        owner.setWaits(Collections.EMPTY_LIST);
        owner.setKeys(Arrays.asList(new Key(newAccountPublicKey, 1)));

        createAccountArg.setOwner(owner);

        // set the active key
        RequiredAuth active = new RequiredAuth();
        active.setThreshold(1L);
        active.setAccounts(Collections.EMPTY_LIST);
        active.setWaits(Collections.EMPTY_LIST);
        active.setKeys(Arrays.asList(new Key(newAccountPublicKey, 1)));

        createAccountArg.setActive(active);
        return createAccountArg;
    }

    public static PushedTransaction createAccount(String endpoint, String privateKey, String creator, String newAccountName, String newAccountPublicKey, int ramByte, BigDecimal net, BigDecimal cpu)
    {
        EosApi eosApi = EosApiFactory.create(endpoint);
        SignArg arg = eosApi.getSignArg(120);

        net = net.setScale(4, RoundingMode.HALF_UP);
        cpu = cpu.setScale(4, RoundingMode.HALF_UP);

        String netString = net.toPlainString() + " EOS";
        String cpuString = cpu.toPlainString() + " EOS";

        // ① build binary of create account
        CreateAccountArg createAccountArg = getCreateAccountArg(creator, newAccountName, newAccountPublicKey);

        AbiJsonToBin createAccountData = eosApi.abiJsonToBin("eosio", "newaccount", createAccountArg);
        System.out.println("create account bin= " + createAccountData.getBinargs());

        // ② build binary of ram
        //BuyRamByteArg buyRamByteArg = new BuyRamByteArg(creator,newAccountName, 1024 * 8);//8k memory
        BuyRamByteArg buyRamByteArg = new BuyRamByteArg(creator,newAccountName, ramByte);//8k memory
        AbiJsonToBin buyRamData = eosApi.abiJsonToBin("eosio","buyrambytes", buyRamByteArg);
        System.out.println("buy ram bin= "+ buyRamData.getBinargs());

        // ③ delegatebw cpu and net
        DelegatebwArg delegatebwArg = new DelegatebwArg(creator, newAccountName, netString, cpuString, 0L);
        AbiJsonToBin delegatebwData = eosApi.abiJsonToBin("eosio", "delegatebw", delegatebwArg);
        System.out.println("delegatebw bin= "+delegatebwData.getBinargs());

        // ④ get the latest block info
        Block block = eosApi.getBlock(eosApi.getChainInfo().getHeadBlockId());
        System.out.println("blockNum=" + block.getBlockNum());

        // ⑤ create the authorization
        List<TransactionAuthorization> authorizations = Arrays.asList(new TransactionAuthorization(creator, "active"));

        // ⑥ build the all actions
        List<TransactionAction> actions = Arrays.asList(//
                new TransactionAction("eosio","newaccount",authorizations, createAccountData.getBinargs())//
                ,new TransactionAction("eosio","buyrambytes",authorizations, buyRamData.getBinargs())//
                ,new TransactionAction("eosio","delegatebw",authorizations, delegatebwData.getBinargs())//
        );

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

    public static PushedTransaction createAccount(String endpoint, String privateKey, String creator, String newAccountName, String newAccountPublicKey, BigDecimal ram, BigDecimal net, BigDecimal cpu, Long delegateBwTransfer)
    {
        EosApi eosApi = EosApiFactory.create(endpoint);
        SignArg arg = eosApi.getSignArg(120);

        ram = ram.setScale(4, RoundingMode.HALF_UP);
        net = net.setScale(4, RoundingMode.HALF_UP);
        cpu = cpu.setScale(4, RoundingMode.HALF_UP);

        String ramString = ram.toPlainString() + " EOS";
        String netString = net.toPlainString() + " EOS";
        String cpuString = cpu.toPlainString() + " EOS";

        // ① build binary of create account
        CreateAccountArg createAccountArg = getCreateAccountArg(creator, newAccountName, newAccountPublicKey);

        AbiJsonToBin createAccountData = eosApi.abiJsonToBin("eosio", "newaccount", createAccountArg);
        System.out.println("create account bin= " + createAccountData.getBinargs());

        // ② build binary of ram
        BuyRamArg buyRamByteArg = new BuyRamArg(creator,newAccountName, ramString);//8k memory
        AbiJsonToBin buyRamData = eosApi.abiJsonToBin("eosio","buyram", buyRamByteArg);
        System.out.println("buy ram bin= "+ buyRamData.getBinargs());

        // ③ delegatebw cpu and net
        DelegatebwArg delegatebwArg = new DelegatebwArg(creator, newAccountName, netString, cpuString, delegateBwTransfer);
        AbiJsonToBin delegatebwData = eosApi.abiJsonToBin("eosio", "delegatebw", delegatebwArg);
        System.out.println("delegatebw bin= "+delegatebwData.getBinargs());

        // ④ get the latest block info
        Block block = eosApi.getBlock(eosApi.getChainInfo().getHeadBlockId());
        System.out.println("blockNum=" + block.getBlockNum());

        // ⑤ create the authorization
        List<TransactionAuthorization> authorizations = Arrays.asList(new TransactionAuthorization(creator, "active"));

        // ⑥ build the all actions
        List<TransactionAction> actions = Arrays.asList(//
                new TransactionAction("eosio","newaccount",authorizations, createAccountData.getBinargs())//
                ,new TransactionAction("eosio","buyram",authorizations, buyRamData.getBinargs())//
                ,new TransactionAction("eosio","delegatebw",authorizations, delegatebwData.getBinargs())//
        );

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

    public static PackedTransaction makeCommonPart(SignArg arg)
    {
        PackedTransaction packedTransaction = new PackedTransaction();
        packedTransaction.setExpiration(arg.getHeadBlockTime().plusSeconds(arg.getExpiredSecond()));
        packedTransaction.setRefBlockNum(arg.getLastIrreversibleBlockNum());
        packedTransaction.setRefBlockPrefix(arg.getRefBlockPrefix());

        packedTransaction.setMaxNetUsageWords(0);
        packedTransaction.setMaxCpuUsageMs(0);
        packedTransaction.setDelaySec(0);

        return packedTransaction;
    }

    public static PushTransactionRequest makePushTransaction(SignArg arg, String privateKey, PackedTransaction packedTransaction)
    {
        String hash = sign(privateKey, arg, packedTransaction);
        PushTransactionRequest req = new PushTransactionRequest();
        req.setTransaction(packedTransaction);
        req.setSignatures(Arrays.asList(hash));
        return req;
    }

    public static PushTransactionRequest processCommon(SignArg arg, String privateKey, String eosAccount, String actionName, String from, String packed)
    {
        List<TransactionAuthorization> authorizations = Arrays.asList(new TransactionAuthorization(from, "active"));
        List<TransactionAction> actions = Arrays.asList(new TransactionAction(eosAccount, actionName, authorizations, packed));
        PackedTransaction packedTransaction = CommonEosApiUtil.makeCommonPart(arg);
        packedTransaction.setActions(actions);
        return CommonEosApiUtil.makePushTransaction(arg, privateKey, packedTransaction);
    }

    public static PushTransactionRequest processEosTransferCommon(SignArg arg, String privateKey, String eosAccount, String actionName, String packed)
    {
        List<TransactionAuthorization> authorizations = Arrays.asList(new TransactionAuthorization(eosAccount, "active"));
        List<TransactionAction> actions = Arrays.asList(new TransactionAction("eosio.token", actionName, authorizations, packed));
        PackedTransaction packedTransaction = CommonEosApiUtil.makeCommonPart(arg);
        packedTransaction.setActions(actions);
        return CommonEosApiUtil.makePushTransaction(arg, privateKey, packedTransaction);
    }
}