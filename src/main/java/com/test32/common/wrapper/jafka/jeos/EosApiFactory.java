package com.test32.common.wrapper.jafka.jeos;

import com.test32.common.wrapper.jafka.jeos.impl.EosApiRestClientImpl;
import com.test32.common.wrapper.jafka.jeos.impl.LocalApiImpl;

public abstract class EosApiFactory
{
    public static EosApi create(String baseUrl) {
        return new EosApiRestClientImpl(baseUrl);
    }
    public static EosApi create(String walletBaseUrl, String chainBaseUrl, String historyBaseUrl) { return new EosApiRestClientImpl(walletBaseUrl, chainBaseUrl, historyBaseUrl); }
    public static LocalApi createLocalApi() { return new LocalApiImpl(); }
}