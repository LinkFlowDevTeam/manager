package com.linkFlow.manager.common.service.impl;

import com.linkFlow.manager.common.dao.Erc20BlockDao;
import com.linkFlow.manager.common.model.vo.Erc20BlockVO;
import com.linkFlow.manager.common.service.Erc20BlockService;
import com.test32.common.paging.PagingData;
import com.test32.common.paging.PagingManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("erc20BlockService")
public class Erc20BlockServiceImpl implements Erc20BlockService
{
    @Autowired private Erc20BlockDao erc20BlockDao;

    @Override public boolean insertNotExist(Erc20BlockVO entity) { return erc20BlockDao.insertNotExist(entity); }
    @Override public boolean insert(Erc20BlockVO entity) { return false; }
    @Override public Erc20BlockVO select(Long index) { return erc20BlockDao.selectByIndex(index); }
    @Override public List<Erc20BlockVO> selectAll() { return erc20BlockDao.selectAll(); }
    @Override public List<Erc20BlockVO> selectBySearch(Map<String, Object> parameter) { return erc20BlockDao.selectBySearch(parameter); }
    @Override public boolean update(Erc20BlockVO entity) { return erc20BlockDao.update(entity); }
    @Override public boolean delete(Long index) { return erc20BlockDao.deleteByIndex(index); }
    @Override public Long countBySearch(Map<String, Object> parameter) { return erc20BlockDao.countBySearch(parameter); }

    @Override
    public Erc20BlockVO getLastFinishedBlock()
    {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("orderColumn", "eb_block_number");
        parameter.put("order", "DESC");
        parameter.put("ebState", Erc20BlockService.STATE_PROCESSED);
        parameter.put("limit", 1);
        List<Erc20BlockVO> list = selectBySearch(parameter);
        if(list.size() > 0)
            return list.get(0);
        return null;
    }

    @Override
    public Erc20BlockVO getWorkingBlock()
    {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("orderColumn", "eb_block_number");
        parameter.put("order", "DESC");
        parameter.put("ebState", Erc20BlockService.STATE_NONE);
        parameter.put("limit", 1);
        List<Erc20BlockVO> list = selectBySearch(parameter);
        if(list.size() > 0)
            return list.get(0);
        return null;
    }

    @Override
    public Erc20BlockVO selectByBlockNumber(long blockNumber)
    {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("ebBlockNumber", blockNumber);
        List<Erc20BlockVO> list = selectBySearch(parameter);
        if(list.size() == 1)
            return list.get(0);
        return null;
    }

    @Override
    public boolean updateByBlockNumber(Erc20BlockVO erc20BlockVO)
    {
        return erc20BlockDao.updateByBlockNumber(erc20BlockVO);
    }

    @Override
    public PagingData selectPagingBySearch(Map<String, Object> parameter)
    {
        String pageNumberStr = (String) parameter.get("pageNo");
        String pageSizeStr = (String) parameter.get("pageSize");
        int pageNumber = StringUtils.isEmpty(pageNumberStr) ? 1 : Integer.valueOf(pageNumberStr);
        int pageSize = StringUtils.isEmpty(pageSizeStr) ? 20 : Integer.valueOf(pageSizeStr);
        Long totalRecode = erc20BlockDao.countBySearch(parameter);

        PagingData pagingData = PagingManager.getPagingList(pageNumber, totalRecode, pageSize, 10);
        parameter.put("startRow", pagingData.getStartRow());
        parameter.put("rowCount", pagingData.getPageSize());

        List<Erc20BlockVO> dataList = selectBySearch(parameter);
        pagingData.setObject(dataList);
        pagingData.setCurrentPageRowCount(dataList.size());

        return pagingData;
    }
}