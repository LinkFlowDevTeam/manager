package com.test32.common.wrapper.jafka.jeos.core.common.transaction;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper=true)
public class SignedPackedTransaction extends PackedTransaction {

    private List<String> signatures;

}
