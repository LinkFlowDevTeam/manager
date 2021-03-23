package com.linkFlow.manager.common.util.eos;

import com.linkFlow.manager.common.util.eos.json2bin.LocalApiImpl_alliancex;
import com.test32.common.wrapper.jafka.jeos.EosApi;
import com.test32.common.wrapper.jafka.jeos.impl.EosApiRestClientImpl;

public abstract class EosApiFactory_alliancex
{
    public static EosApi create(String baseUrl) {
        return new EosApiRestClientImpl(baseUrl);
    }
    public static EosApi create(String walletBaseUrl, String chainBaseUrl, String historyBaseUrl) { return new EosApiRestClientImpl(walletBaseUrl, chainBaseUrl, historyBaseUrl); }
    public static LocalApi_alliancex createLocalApi() {
        return new LocalApiImpl_alliancex();
    }
}
