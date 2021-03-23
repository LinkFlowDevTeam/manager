package com.test32.common.wrapper.jafka.jeos.impl;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okio.Buffer;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class LoggingInterceptor implements Interceptor {
    static AtomicLong _seq = new AtomicLong(0);

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        final long index = _seq.incrementAndGet();
        long t1 = System.currentTimeMillis();
        logger.info("OkHttp-{}-1-->head {} {}", index, request.url(), request.headers());

        if (request.body() != null) {
            Buffer requestBuffer = new Buffer();
            request.body().writeTo(requestBuffer);
            logger.info("OkHttp-{}-2-->body {}", index, requestBuffer.readUtf8());
        }

        Response response = chain.proceed(request);

        long t2 = System.currentTimeMillis();
        logger.info("OkHttp-{}-3--<head {} {}ms {}", index, request.url(), (t2 - t1), response.headers());

        MediaType contentType = response.body().contentType();
        String content = response.body().string();
        logger.info("OkHttp-{}-4--<body {}", index, content);

        ResponseBody wrappedBody = ResponseBody.create(contentType, content);
        return response.newBuilder().body(wrappedBody).build();
    }
}