package com.linkFlow.manager.common.service.impl;

import com.linkFlow.manager.common.dao.MemberTierDao;
import com.linkFlow.manager.common.model.vo.MemberTierVO;
import com.linkFlow.manager.common.model.vo.MemberTierVOForApi;
import com.linkFlow.manager.common.service.MemberTierService;
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
public class MemberTierServiceImpl implements MemberTierService
{
    @Autowired private MemberTierDao memberTierDao;

    @Override public boolean insert(MemberTierVO entity) { return memberTierDao.insert(entity); }
    @Override public MemberTierVO select(Integer index) { return memberTierDao.selectByIndex(index); }
    @Override public List<MemberTierVO> selectAll() { return memberTierDao.selectAll(); }
    @Override public List<MemberTierVO> selectBySearch(Map<String, Object> parameter) { return memberTierDao.selectBySearch(parameter); }
    @Override public boolean update(MemberTierVO entity) { return memberTierDao.update(entity); }
    @Override public boolean delete(Integer index) { return memberTierDao.deleteByIndex(index); }

    @Override
    public MemberTierVO selectByTier(int tier)
    {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("mtTier", tier);
        List<MemberTierVO> list = memberTierDao.selectBySearch(parameter);
        if(list.size() == 1)
            return list.get(0);
        return null;
    }

    @Override
    public MemberTierVO selectByAmount(BigDecimal amount)
    {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("orderColumn", "mt_tier");
        parameter.put("order", "DESC");
        parameter.put("mtDepositAmountUnder", amount);

        List<MemberTierVO> list = memberTierDao.selectBySearch(parameter);
        if(list.size() > 0)
            return list.get(0);

        parameter.put("orderColumn", "mt_tier");
        parameter.put("order", "ASC");
        parameter.remove("mtDepositAmountUnder");
        list = memberTierDao.selectBySearch(parameter);
        if(list.size() > 0)
            return list.get(0);
        return null;
    }

    @Override
    public MemberTierVO selectByRate(BigDecimal rate)
    {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("orderColumn", "mt_tier");
        parameter.put("order", "DESC");


        List<MemberTierVO> list = memberTierDao.selectBySearch(parameter);
        MemberTierVO selected = list.get(0);
        for(MemberTierVO item : list)
        {
            if(item.getMtRate().compareTo(rate) > 0)
            {
                selected = item;
            }
        }
        return selected;
    }

    @Override
    public PagingData selectPagingBySearch(Map<String, Object> parameter)
    {
        String pageNumberStr = (String) parameter.get("pageNo");
        String pageSizeStr = (String) parameter.get("pageSize");
        int pageNumber = StringUtils.isEmpty(pageNumberStr) ? 1 : Integer.valueOf(pageNumberStr);
        int pageSize = StringUtils.isEmpty(pageSizeStr) ? 20 : Integer.valueOf(pageSizeStr);
        int totalRecode = memberTierDao.countBySearch(parameter);

        PagingData pagingData = PagingManager.getPagingList(pageNumber, Long.valueOf("" + totalRecode), pageSize, 10);
        parameter.put("startRow", pagingData.getStartRow());
        parameter.put("rowCount", pagingData.getPageSize());

        List<MemberTierVO> dataList = selectBySearch(parameter);
        pagingData.setObject(dataList);
        pagingData.setCurrentPageRowCount(dataList.size());

        return pagingData;
    }

    @Override
    public List<MemberTierVOForApi> selectBySearchForApi(Map<String, Object> parameter)
    {
        return memberTierDao.selectBySearchForApi(parameter);
    }
}