package com.test32.common.wrapper.jafka.jeos.core.response.history.controlledaccounts;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ControlledAccounts {

    private List<String> controlledAccounts;

    public List<String> getControlledAccounts() {
        return controlledAccounts;
    }

    @JsonProperty("controlled_accounts")
    public void setControlledAccounts(List<String> controlledAccounts) {
        this.controlledAccounts = controlledAccounts;
    }

}
