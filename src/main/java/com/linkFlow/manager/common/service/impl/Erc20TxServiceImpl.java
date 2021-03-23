package com.linkFlow.manager.common.service.impl;

import com.linkFlow.manager.common.config.ConfigDataManager;
import com.linkFlow.manager.common.dao.Erc20TxDao;
import com.linkFlow.manager.common.model.vo.Erc20TxVO;
import com.linkFlow.manager.common.service.Erc20TxService;
import com.test32.common.paging.PagingData;
import com.test32.common.paging.PagingManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("erc20TxService")
@Transactional("apiTransactionManager")
public class Erc20TxServiceImpl implements Erc20TxService
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private ConfigDataManager configDataManager;
    @Autowired
    private Erc20TxDao erc20TxDao;

    @Override
    public long countByBlockNumber(long blockNumber)
    {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("etBlockNumber", blockNumber);
        return erc20TxDao.countBySearch(parameter);
    }

    @Override
    public boolean insertNotExist(Erc20TxVO entity)
    {
        return erc20TxDao.insertNotExist(entity);
    }

    @Override
    public boolean insert(Erc20TxVO entity)
    {
        return false;
    }

    @Override
    public Erc20TxVO select(Long index)
    {
        return erc20TxDao.selectByIndex(index);
    }

    @Override
    public List<Erc20TxVO> selectAll()
    {
        return erc20TxDao.selectAll();
    }

    @Override
    public List<Erc20TxVO> selectBySearch(Map<String, Object> parameter)
    {
        return erc20TxDao.selectBySearch(parameter);
    }

    @Override
    public PagingData selectPagingBySearch(Map<String, Object> parameter)
    {
        String pageNumberStr = (String) parameter.get("pageNo");
        String pageSizeStr = (String) parameter.get("pageSize");
        int pageNumber = StringUtils.isEmpty(pageNumberStr) ? 1 : Integer.valueOf(pageNumberStr);
        int pageSize = StringUtils.isEmpty(pageSizeStr) ? 20 : Integer.valueOf(pageSizeStr);
        Long totalRecode = erc20TxDao.countBySearch(parameter);

        PagingData pagingData = PagingManager.getPagingList(pageNumber, totalRecode, pageSize, 10);
        parameter.put("startRow", pagingData.getStartRow());
        parameter.put("rowCount", pagingData.getPageSize());

        List<Erc20TxVO> dataList = selectBySearch(parameter);
        pagingData.setObject(dataList);
        pagingData.setCurrentPageRowCount(dataList.size());

        return pagingData;
    }

    @Override
    public boolean update(Erc20TxVO entity)
    {
        return erc20TxDao.update(entity);
    }

    @Override
    public boolean delete(Long index)
    {
        return erc20TxDao.deleteByIndex(index);
    }

    @Override
    public boolean deleteUseless(int searchSecond)
    {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("searchSecond", searchSecond);
        return erc20TxDao.deleteUseless(parameter);
    }
}