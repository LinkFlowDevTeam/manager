package com.test32.common.model.blockChain.etherscan;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ResEtherScanEthPriceData
{
@JsonProperty("ethbtc")  BigDecimal ethbtc;
@JsonProperty("ethbtc_timestamp")  BigDecimal ethbtc_timestamp;
@JsonProperty("ethusd")  BigDecimal ethusd;
@JsonProperty("ethusd_timestamp")  BigDecimal ethusd_timestamp;
}