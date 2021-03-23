package com.linkFlow.manager.common.service.impl;

import com.linkFlow.manager.common.config.CommonConstants;
import com.linkFlow.manager.common.dao.CommonTicketDao;
import com.linkFlow.manager.common.model.vo.CommonTicketVO;
import com.linkFlow.manager.common.service.CommonTicketService;
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
public class CommonTicketServiceImpl implements CommonTicketService
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CommonTicketDao commonTicketDao;
    @Autowired
    private CustomComponentUtil customComponentUtil;

    @Override
    public boolean insert(CommonTicketVO entity)
    {
        return commonTicketDao.insert(entity);
    }

    @Override
    public CommonTicketVO select(Long index)
    {
        return commonTicketDao.selectByIndex(index);
    }

    @Override
    public List<CommonTicketVO> selectAll()
    {
        return commonTicketDao.selectAll();
    }

    @Override
    public List<CommonTicketVO> selectBySearch(Map<String, Object> parameter)
    {
        return commonTicketDao.selectBySearch(parameter);
    }

    @Override
    public PagingData selectPagingBySearch(Map<String, Object> parameter)
    {
        String pageNumberStr = (String) parameter.get("pageNo");
        String pageSizeStr = (String) parameter.get("pageSize");
        int pageNumber = StringUtils.isEmpty(pageNumberStr) ? 1 : Integer.valueOf(pageNumberStr);
        int pageSize = StringUtils.isEmpty(pageSizeStr) ? 20 : Integer.valueOf(pageSizeStr);
        Long totalRecode = commonTicketDao.countBySearch(parameter);

        PagingData pagingData = PagingManager.getPagingList(pageNumber, totalRecode, pageSize, 10);
        parameter.put("startRow", pagingData.getStartRow());
        parameter.put("rowCount", pagingData.getPageSize());

        List<CommonTicketVO> dataList = selectBySearch(parameter);
        pagingData.setObject(dataList);
        pagingData.setCurrentPageRowCount(dataList.size());

        return pagingData;
    }

    @Override
    public boolean update(CommonTicketVO entity)
    {
        return commonTicketDao.update(entity);
    }

    @Override
    public boolean delete(Long index)
    {
        return false;
    }

    @Override
    public CommonTicketVO generate(Long mbIdx)
    {
        for(int i = 0; i < 10; i++)
        {
            try
            {
                CommonTicketVO entity = new CommonTicketVO();
                entity.setTkRequestData("" + mbIdx);
                entity.setTkUserId(customComponentUtil.generateRandomForTicketId(CommonConstants.TICKET_ID_LENGTH));
                entity.setTkState(CommonTicketService.STATE_ACTIVE);
                entity.setTkSign(customComponentUtil.generateRandomForTicketSign(CommonConstants.TICKET_ID_LENGTH));
                if(commonTicketDao.insert(entity))
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
    public CommonTicketVO selectByRequestId(String genId)
    {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("tkUserId", genId);
        List<CommonTicketVO> list = selectBySearch(parameter);
        if(list.size() == 1)
            return list.get(0);
        return null;
    }

    @Override
    public boolean checkSign(String genId, String sign)
    {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("tkUserId", genId);
        parameter.put("tkSign", sign);
        parameter.put("tkState", CommonTicketService.STATE_ACTIVE);
        List<CommonTicketVO> list = selectBySearch(parameter);
        return (list.size() > 0);
    }

    @Override
    public boolean updateDate(Long tkIdx)
    {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("tkIdx", tkIdx);
        return commonTicketDao.updateDate(parameter);
    }

    @Override
    public boolean increaseErrorCount(Long tkIdx)
    {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("tkIdx", tkIdx);
        return commonTicketDao.increaseErrorCount(parameter);
    }

    @Override
    public boolean expire(Long tkIdx)
    {
        CommonTicketVO forUpdate = new CommonTicketVO();
        forUpdate.setTkIdx(tkIdx);
        forUpdate.setTkState(CommonTicketService.STATE_EXPIRED);
        return commonTicketDao.update(forUpdate);
    }

    @Override
    public boolean deleteOverSecond(int second)
    {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("searchSecond", second);
        return commonTicketDao.deleteOverSecond(parameter);
    }
}