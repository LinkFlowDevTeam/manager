package com.test32.common.wrapper.jafka.jeos.core.response.chain.abi;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Abi {

    private String accountName;

    private com.test32.common.wrapper.jafka.jeos.core.response.chain.code.Abi abi;


    public String getAccountName() {
        return accountName;
    }

    @JsonProperty("account_name")
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public com.test32.common.wrapper.jafka.jeos.core.response.chain.code.Abi getAbi() {
        return abi;
    }

    public void setAbi(com.test32.common.wrapper.jafka.jeos.core.response.chain.code.Abi abi) {
        this.abi = abi;
    }

}
