package com.test32.common.wrapper.jafka.jeos.impl;

import com.test32.common.wrapper.jafka.jeos.core.request.history.TransactionRequest;
import com.test32.common.wrapper.jafka.jeos.core.response.history.action.Actions;
import com.test32.common.wrapper.jafka.jeos.core.response.history.controlledaccounts.ControlledAccounts;
import com.test32.common.wrapper.jafka.jeos.core.response.history.keyaccounts.KeyAccounts;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

import java.util.Map;

public interface EosHistoryApiService {

    @POST("/v1/history/get_actions")
    Call<Actions> getActions(@Body Map<String, Object> requestFields);

    @POST("/v1/history/get_transaction")
    Call<Object> getTransaction(@Body TransactionRequest transactionRequest);

    @POST("/v1/history/get_key_accounts")
    Call<KeyAccounts> getKeyAccounts(@Body Map<String, String> requestFields);

    @POST("/v1/history/get_controlled_accounts")
    Call<ControlledAccounts> getControlledAccounts(@Body Map<String, String> requestFields);

}
