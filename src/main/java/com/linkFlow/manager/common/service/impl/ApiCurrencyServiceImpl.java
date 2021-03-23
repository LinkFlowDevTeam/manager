package com.linkFlow.manager.common.service.impl;

import com.linkFlow.manager.common.dao.ApiCurrencyDao;
import com.linkFlow.manager.common.model.vo.ApiCurrencyVO;
import com.linkFlow.manager.common.service.ApiCurrencyService;
import com.test32.common.paging.PagingData;
import com.test32.common.paging.PagingManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("apiCurrencyService")
public class ApiCurrencyServiceImpl implements ApiCurrencyService
{
    @Autowired private ApiCurrencyDao apiCurrencyDao;

    @Override public boolean insert(ApiCurrencyVO entity) { return apiCurrencyDao.insert(entity); }
    @Override public ApiCurrencyVO selectByIndex(Long index) { return apiCurrencyDao.selectByIndex(index); }
    @Override public List<ApiCurrencyVO> selectAll() { return apiCurrencyDao.selectAll(); }
    @Override public List<ApiCurrencyVO> selectBySearch(Map<String, Object> parameter) { return apiCurrencyDao.selectBySearch(parameter); }
    @Override public boolean update(ApiCurrencyVO entity) { return apiCurrencyDao.update(entity); }
    @Override public boolean deleteByIndex(Long index) { return apiCurrencyDao.deleteByIndex(index); }
    @Override public boolean insertOrUpdate(ApiCurrencyVO apiCurrencyVO) { return apiCurrencyDao.insertOrUpdate(apiCurrencyVO); }
    @Override public Long countBySearch(Map<String, Object> parameter) { return apiCurrencyDao.countBySearch(parameter); }
    @Override public Long countAll() { return apiCurrencyDao.countAll(); }

    @Override
    public ApiCurrencyVO selectByCurrency(String currency)
    {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("cur_unit", currency);
        List<ApiCurrencyVO> list = selectBySearch(parameter);
        if(list.size() == 1)
            return list.get(0);
        return null;
    }

    @Override
    public PagingData selectPagingBySearch(Map<String, Object> parameter)
    {
        String pageNumberStr = (String) parameter.get("pageNo");
        String pageSizeStr = (String) parameter.get("pageSize");
        int pageNumber = StringUtils.isEmpty(pageNumberStr) ? 1 : Integer.valueOf(pageNumberStr);
        int pageSize = StringUtils.isEmpty(pageSizeStr) ? 20 : Integer.valueOf(pageSizeStr);
        Long totalRecode = apiCurrencyDao.countBySearch(parameter);

        PagingData pagingData = PagingManager.getPagingList(pageNumber, totalRecode, pageSize, 10);
        parameter.put("startRow", pagingData.getStartRow());
        parameter.put("rowCount", pagingData.getPageSize());

        List<ApiCurrencyVO> dataList = apiCurrencyDao.selectBySearch(parameter);
        pagingData.setObject(dataList);
        pagingData.setCurrentPageRowCount(dataList.size());

        return pagingData;
    }
}