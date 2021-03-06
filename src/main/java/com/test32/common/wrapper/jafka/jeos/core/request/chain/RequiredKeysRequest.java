package com.test32.common.wrapper.jafka.jeos.core.request.chain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.test32.common.wrapper.jafka.jeos.core.common.transaction.PackedTransaction;

import java.util.List;

public class RequiredKeysRequest {

    private PackedTransaction transaction;

    private List<String> availableKeys;

    public RequiredKeysRequest(PackedTransaction transaction, List<String> availableKeys) {
        this.transaction = transaction;
        this.availableKeys = availableKeys;
    }

    public PackedTransaction getTransaction() {
        return transaction;
    }

    public void setTransaction(PackedTransaction transaction) {
        this.transaction = transaction;
    }

    @JsonProperty("available_keys")
    public List<String> getAvailableKeys() {
        return availableKeys;
    }

    public void setAvailableKeys(List<String> availableKeys) {
        this.availableKeys = availableKeys;
    }
}
