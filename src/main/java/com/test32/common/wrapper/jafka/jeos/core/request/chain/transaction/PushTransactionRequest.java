package com.test32.common.wrapper.jafka.jeos.core.request.chain.transaction;

import com.test32.common.wrapper.jafka.jeos.core.common.transaction.PackedTransaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PushTransactionRequest {

    private String compression = "none";
    private PackedTransaction transaction;
    private List<String> signatures;

}
