
package com.test32.common.wrapper.jafka.jeos.core.response.history.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Data {

    private Header header;

    public Header getHeader() {
        return header;
    }

    @JsonProperty("header")
    public void setHeader(Header header) {
        this.header = header;
    }

}
