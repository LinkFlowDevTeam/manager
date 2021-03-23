package com.linkFlow.manager.common.service;

import com.linkFlow.manager.common.model.vo.CoinmarketcapVO;
import com.test32.common.generic.GenericService;

import java.util.List;

public interface CoinmarketcapService extends GenericService<CoinmarketcapVO>
{
    List<CoinmarketcapVO> selectTickerEnabled();
    List<CoinmarketcapVO> selectChartEnabled();
}