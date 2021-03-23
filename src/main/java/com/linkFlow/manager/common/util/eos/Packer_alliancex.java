package com.linkFlow.manager.common.util.eos;

import com.linkFlow.manager.common.util.eos.json2bin.AlliancexLock;
import com.linkFlow.manager.common.util.eos.json2bin.AlliancexUnlock;
import com.test32.common.wrapper.jafka.jeos.convert.Packer;
import com.test32.common.wrapper.jafka.jeos.util.Raw;

public class Packer_alliancex extends Packer
{
    public static String packLock(AlliancexLock arg)
    {
        Raw raw = new Raw();
        raw.packName(arg.getUser());
        raw.packAsset(arg.getQuantity());
        return raw.toHex();
    }

    public static String packUnlock(AlliancexUnlock arg)
    {
        Raw raw = new Raw();
        raw.packName(arg.getUser());
        raw.packAsset(arg.getQuantity());
        return raw.toHex();
    }
}