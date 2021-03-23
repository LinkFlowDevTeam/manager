package com.linkFlow.manager.common.service.impl;

import com.linkFlow.manager.common.dao.CoinmarketcapDao;
import com.linkFlow.manager.common.model.vo.CoinmarketcapVO;
import com.linkFlow.manager.common.service.CoinmarketcapService;
import com.test32.common.paging.PagingData;
import com.test32.common.paging.PagingManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CoinmarketcapServiceImpl implements CoinmarketcapService
{
    @Autowired private CoinmarketcapDao coinmarketcapDao;

    @Override public boolean insert(CoinmarketcapVO entity) { return coinmarketcapDao.insert(entity); }
    @Override public CoinmarketcapVO select(Integer index) { return coinmarketcapDao.selectByIndex(index); }
    @Override public List<CoinmarketcapVO> selectAll() { return coinmarketcapDao.selectAll(); }
    @Override public List<CoinmarketcapVO> selectBySearch(Map<String, Object> parameter) { return coinmarketcapDao.selectBySearch(parameter); }
    @Override public boolean update(CoinmarketcapVO entity) { return coinmarketcapDao.update(entity); }
    @Override public boolean delete(Integer index) { return coinmarketcapDao.deleteByIndex(index); }

    @Override
    public PagingData selectPagingBySearch(Map<String, Object> parameter)
    {
        String pageNumberStr = (String) parameter.get("pageNo");
        String pageSizeStr = (String) parameter.get("pageSize");
        int pageNumber = StringUtils.isEmpty(pageNumberStr) ? 1 : Integer.valueOf(pageNumberStr);
        int pageSize = StringUtils.isEmpty(pageSizeStr) ? 20 : Integer.valueOf(pageSizeStr);
        int totalRecode = coinmarketcapDao.countBySearch(parameter);

        PagingData pagingData = PagingManager.getPagingList(pageNumber, Long.valueOf("" + totalRecode), pageSize, 10);
        parameter.put("startRow", pagingData.getStartRow());
        parameter.put("rowCount", pagingData.getPageSize());

        List<CoinmarketcapVO> dataList = selectBySearch(parameter);
        pagingData.setObject(dataList);
        pagingData.setCurrentPageRowCount(dataList.size());

        return pagingData;
    }

    @Override
    public List<CoinmarketcapVO> selectTickerEnabled()
    {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("ckTickerState", 1);
        return selectBySearch(parameter);
    }

    @Override
    public List<CoinmarketcapVO> selectChartEnabled()
    {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("ckChartState", 1);
        return selectBySearch(parameter);
    }
}