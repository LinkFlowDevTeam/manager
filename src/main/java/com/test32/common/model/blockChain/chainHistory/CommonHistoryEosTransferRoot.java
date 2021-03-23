package com.test32.common.model.blockChain.chainHistory;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
public class CommonHistoryEosTransferRoot
{
    @JsonProperty("data")
    private CommonHistoryEosTransferData data;

    @JsonProperty("account")
    private String account;

    @JsonProperty("name")
    private String name;


}

// {"authorization":[{"actor":"eosfaucet111","permission":"active"}],"data":{"from":"eosfaucet111","to":"sevenluckyio","amount":100,"symbol":"EOS","memo":""},"account":"eosio.token","name":"transfer"}