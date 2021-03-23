package com.test32.common.wrapper.jafka.jeos.core.response.history.transaction;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.Optional;

/**
 * 
 * @author adyliu (imxylz@gmail.com)
 * @since 2018年9月14日
 */
public class TrxDeserializer extends StdDeserializer<Optional<Trx>> {

    public TrxDeserializer() {
        super(Trx.class);
    }

    @Override
    public Optional<Trx> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException
    {
        return Optional.ofNullable(p.isExpectedStartObjectToken() ? p.readValueAs(Trx.class) : null);
    }
}
