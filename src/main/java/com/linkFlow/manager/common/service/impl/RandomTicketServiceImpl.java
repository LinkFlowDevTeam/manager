package com.linkFlow.manager.common.service.impl;

import com.linkFlow.manager.common.dao.RandomTicketDao;
import com.linkFlow.manager.common.model.vo.RandomTicketVO;
import com.linkFlow.manager.common.service.RandomTicketService;
import com.linkFlow.manager.common.util.CustomComponentUtil;
import com.test32.common.paging.PagingData;
import com.test32.common.paging.PagingManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RandomTicketServiceImpl implements RandomTicketService
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired private RandomTicketDao randomTicketDao;
    @Autowired private CustomComponentUtil customComponentUtil;

    @Override public boolean insert(RandomTicketVO entity) { return randomTicketDao.insert(entity); } 
    @Override public RandomTicketVO select(Long index) { return randomTicketDao.selectByIndex(index); }
    @Override public List<RandomTicketVO> selectAll() { return randomTicketDao.selectAll(); }
    @Override public List<RandomTicketVO> selectBySearch(Map<String, Object> parameter) { return randomTicketDao.selectBySearch(parameter); }
    @Override public boolean update(RandomTicketVO entity) { return randomTicketDao.update(entity); }
    @Override public boolean delete(Long index) { return false; }
    
    @Override
    public PagingData selectPagingBySearch(Map<String, Object> parameter)
    {
        String pageNumberStr = (String) parameter.get("pageNo");
        String pageSizeStr = (String) parameter.get("pageSize");
        int pageNumber = StringUtils.isEmpty(pageNumberStr) ? 1 : Integer.valueOf(pageNumberStr);
        int pageSize = StringUtils.isEmpty(pageSizeStr) ? 20 : Integer.valueOf(pageSizeStr);
        Long totalRecode = randomTicketDao.countBySearch(parameter);

        PagingData pagingData = PagingManager.getPagingList(pageNumber, totalRecode, pageSize, 10);
        parameter.put("startRow", pagingData.getStartRow());
        parameter.put("rowCount", pagingData.getPageSize());

        List<RandomTicketVO> dataList = selectBySearch(parameter);
        pagingData.setObject(dataList);
        pagingData.setCurrentPageRowCount(dataList.size());

        return pagingData;
    }

    @Override
    public RandomTicketVO generate(String from, String key)
    {
        for(int i = 0; i < 10; i++)
        {
            try
            {
                RandomTicketVO entity = new RandomTicketVO();
                entity.setRtKey(key);
                entity.setRtFrom(from);
                entity.setRtTime(customComponentUtil.getDatabaseNow().getTime());
                if(randomTicketDao.insert(entity))
                    return entity;
            }
            catch (Exception e)
            {
                logger.error(e.toString());
            }
        }
        return null;
    }

    @Override
    public boolean expire(Long idx)
    {
        RandomTicketVO forUpdate = new RandomTicketVO();
        forUpdate.setRtIdx(idx);
        forUpdate.setRtState(RandomTicketService.STATE_EXPIRED);
        return randomTicketDao.update(forUpdate);
    }

    @Override public boolean expireByKey(String key) { Map<String, Object> parameter = new HashMap<>(); parameter.put("rtKey", key); return randomTicketDao.deleteByKey(parameter); }
    @Override public boolean deleteOverSecond(int second) { Map<String, Object> parameter = new HashMap<>(); parameter.put("searchSecond", second); return randomTicketDao.deleteOverSecond(parameter); }
    @Override public boolean deleteExpired() { Map<String, Object> parameter = new HashMap<>(); return randomTicketDao.deleteExpired(parameter); }
}