package com.test32.common.wrapper.jafka.jeos.core.response.chain.code;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Action {

    private String ricardianContract;

    private String name;

    private String type;

    public Action() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("ricardian_contract")
    public String getRicardianContract() {
        return ricardianContract;
    }

    public void setRicardianContract(String ricardianContract) {
        this.ricardianContract = ricardianContract;
    }
}
