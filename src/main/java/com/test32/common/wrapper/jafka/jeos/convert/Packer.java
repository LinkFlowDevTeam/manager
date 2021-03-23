package com.test32.common.wrapper.jafka.jeos.convert;

import com.test32.common.wrapper.jafka.jeos.core.common.transaction.PackedTransaction;
import com.test32.common.wrapper.jafka.jeos.core.request.chain.json2bin.*;
import com.test32.common.wrapper.jafka.jeos.util.Raw;
import com.test32.common.wrapper.jafka.jeos.util.ecc.Hex;

import java.time.ZoneOffset;
import java.util.Arrays;

public class Packer {

    public static String packString(String arg) {
        Raw raw = new Raw();
        raw.pack(arg);
        return raw.toHex();
    }

    public static String packSetAbi(SetAbiArg arg) {
        Raw raw = new Raw();
        raw.packName(arg.getAccount());
        raw.packHexString(arg.getAbi());
        return raw.toHex();
    }

    public static String packSetCodeFromHexString(SetCodeFromStringArg arg) {
        Raw raw = new Raw();
        raw.packName(arg.getAccount());
        raw.packUint8(arg.getVmtype());
        raw.packUint8(arg.getVmversion());
        raw.packHexString(arg.getCode());
        return raw.toHex();
    }
    public static String packSetCodeFromByte(SetCodeFromByteArg arg) {
        Raw raw = new Raw();
        raw.packName(arg.getAccount());
        raw.packUint8(arg.getVmtype());
        raw.packUint8(arg.getVmversion());
        raw.packByteArray(arg.getCode());
        return raw.toHex();
    }

    public static String packTransfer(TransferArg arg) {
        Raw raw = new Raw();
        raw.packName(arg.getFrom());
        raw.packName(arg.getTo());
        raw.packAsset(arg.getQuantity());
        raw.pack(arg.getMemo());
        return raw.toHex();
    }

    public static String packBuyram(BuyRamArg arg) {
        Raw raw = new Raw();
        raw.packName(arg.getPayer());
        raw.packName(arg.getReceiver());
        raw.packAsset(arg.getQuant());
        return raw.toHex();
    }

    public static String packBuyrambytes(BuyRamByteArg arg) {
        Raw raw = new Raw();
        raw.packName(arg.getPayer());
        raw.packName(arg.getReceiver());
        raw.pack(arg.getBytes());
        return raw.toHex();
    }
    public static String packDelegatebw(DelegatebwArg arg) {
        Raw raw = new Raw();
        raw.packName(arg.getFrom());
        raw.packName(arg.getReceiver());
        raw.packAsset(arg.getStakeNetQuantity());
        raw.packAsset(arg.getStakeCpuQuantity());
        raw.packVarint32(arg.getTransfer());
        return raw.toHex();
    }
    
    public static String packCreateAccount(CreateAccountArg arg) {
        Raw raw = new Raw();
        raw.packName(arg.getCreator());
        raw.packName(arg.getName());

        Arrays.asList(arg.getOwner(), arg.getActive()).stream().filter(x -> x != null).forEach(r -> {
            // owner
            raw.packUint32(r.getThreshold());
            // ownwer.keys
            raw.packVarint32(r.getKeys().size());
            r.getKeys().forEach(k -> {
                raw.packPublicKey(k.getKey());
                raw.packUint16(k.getWeight());
            });
            // ownwer.accounts
            raw.packVarint32(r.getAccounts().size());
            r.getAccounts().forEach(a -> {
                raw.packName(a.getPermission().getActor());
                raw.packName(a.getPermission().getPermission());
                raw.packUint16(a.getWeight());
            });
            // ownwer.waits
            raw.packVarint32(r.getWaits().size());
            r.getWaits().forEach(w -> {
                raw.packUint32(w.getWeightSec());
                raw.packUint16(w.getWeight());
            });
        });
        return raw.toHex();
    }
    public static Raw packPackedTransaction(String chainId, PackedTransaction t) {
        Raw raw = new Raw();
        //chain
        raw.pack(Hex.toBytes(chainId));
        //expiration
        raw.packUint32(t.getExpiration().toEpochSecond(ZoneOffset.ofHours(0)));
        //ref_block_num
        raw.packUint16(t.getRefBlockNum().intValue());
        //ref_block_prefix
        raw.packUint32(t.getRefBlockPrefix());
        //max_net_usage_words
        raw.packVarint32(t.getMaxNetUsageWords());
        //max_cpu_usage_ms
        raw.packUint8(t.getMaxCpuUsageMs());//TODO: what the type?
        //delay_sec
        raw.packVarint32(t.getDelaySec());
        //context_free_actions
        raw.packVarint32(t.getContextFreeActions().size());
        //TODO: getContextFreeActions
        
        //actions
        raw.packVarint32(t.getActions().size());
        t.getActions().forEach(a -> {
            //action.account
            raw.packName(a.getAccount())//
                    .packName(a.getName())//
                    .packVarint32(a.getAuthorization().size())//
            ;
            //action.authorization
            a.getAuthorization().forEach(au -> {
                raw.packName(au.getActor())//
                        .packName(au.getPermission());
            });
            //action.data
            byte[] dat = Hex.toBytes(a.getData());
            raw.packVarint32(dat.length);
            raw.pack(dat);
        });
        //transaction_extensions
        //raw.packVarint32(t.getTransactionExtensions().size());
        //TODO: getTransactionExtensions
        
        //context_free_data
        //raw.packVarint32(t.getContextFreeActions().size());
        return raw;
    }
    
}
