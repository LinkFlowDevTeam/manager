package com.linkFlow.manager.common.service.impl;

import com.linkFlow.manager.common.dao.ProductUserDao;
import com.linkFlow.manager.common.model.vo.ProductInfoVO;
import com.linkFlow.manager.common.model.vo.ProductUserVO;
import com.linkFlow.manager.common.model.vo.ProductUserVOForApi;
import com.linkFlow.manager.common.model.vo.ProductUserVOForSum;
import com.linkFlow.manager.common.service.ProductUserService;
import com.test32.common.paging.PagingData;
import com.test32.common.paging.PagingManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductUserServiceImpl implements ProductUserService
{
    @Autowired private ProductUserDao productUserDao;

    @Override public Integer countBySearch(Map<String, Object> parameter) { return productUserDao.countBySearch(parameter); }
    @Override public boolean update(ProductUserVO entity) { return productUserDao.update(entity); }
    @Override public boolean delete(Integer index) { return productUserDao.deleteByIndex(index); }
    @Override public boolean insert(ProductUserVO entity) { return productUserDao.insert(entity); }
    @Override public ProductUserVO select(Integer index) { return productUserDao.selectByIndex(index); }
    @Override public List<ProductUserVO> selectAll() { return productUserDao.selectAll(); }
    @Override public List<ProductUserVO> selectBySearch(Map<String, Object> parameter) { return productUserDao.selectBySearch(parameter); }

    @Override
    public PagingData selectPagingBySearch(Map<String, Object> parameter)
    {
        String pageNumberStr = (String) parameter.get("pageNo");
        String pageSizeStr = (String) parameter.get("pageSize");
        int pageNumber = StringUtils.isEmpty(pageNumberStr) ? 1 : Integer.valueOf(pageNumberStr);
        int pageSize = StringUtils.isEmpty(pageSizeStr) ? 20 : Integer.valueOf(pageSizeStr);
        int totalRecode = productUserDao.countBySearch(parameter);

        PagingData pagingData = PagingManager.getPagingList(pageNumber, Long.valueOf("" + totalRecode), pageSize, 10);
        parameter.put("startRow", pagingData.getStartRow());
        parameter.put("rowCount", pagingData.getPageSize());

        List<ProductUserVO> dataList = selectBySearch(parameter);
        pagingData.setObject(dataList);
        pagingData.setCurrentPageRowCount(dataList.size());

        return pagingData;
    }

    @Override
    public List<ProductUserVOForApi> selectBySearchForApi(Map<String, Object> parameter)
    {
        return productUserDao.selectBySearchForApi(parameter);
    }

    @Override
    public ProductUserVO selectByMemberProduct(long mbIdx, int pdIdx)
    {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("puMbIdx", mbIdx);
        parameter.put("puPdIdx", pdIdx);
        List<ProductUserVO> list = selectBySearch(parameter);
        if(list.size() == 1)
            return list.get(0);
        return null;
    }

    @Override
    public boolean appendBaseAmount(ProductUserVO entity, BigDecimal amount)
    {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("puIdx", entity.getPuIdx());
        parameter.put("amount", amount);
        return productUserDao.appendBaseAmount(parameter);
    }

    @Override
    public boolean appendInterestAmount(ProductUserVO entity, BigDecimal amount)
    {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("puIdx", entity.getPuIdx());
        parameter.put("amount", amount);
        return productUserDao.appendInterestAmount(parameter);
    }

    @Override
    public ProductUserVOForSum selectSumDepositAmount(Long mbIdx, Integer pdIdx)
    {
        Map<String, Object> parameter = new HashMap<>();
        if(mbIdx != null)
            parameter.put("puMbIdx", mbIdx);
        if(pdIdx != null)
            parameter.put("puPdIdx", pdIdx);
        return productUserDao.selectSumBySearch(parameter);
    }

    @Override
    public boolean startProduct(ProductInfoVO productInfoVO)
    {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("puPdIdx", productInfoVO.getPdIdx());
        parameter.put("puState", ProductUserService.STATE_ACTIVE);
        return productUserDao.startProduct(parameter);
    }

    @Override
    public boolean applyDailyInterest(ProductInfoVO productInfoVO, ProductUserVO productUserVO, BigDecimal dailyInterest, int interestIndex)
    {
        return appendInterestAmount(productUserVO, dailyInterest);
    }
}