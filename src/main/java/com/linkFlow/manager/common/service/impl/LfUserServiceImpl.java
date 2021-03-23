package com.linkFlow.manager.common.service.impl;

import com.linkFlow.manager.common.dao.LfUserDao;
import com.linkFlow.manager.common.model.vo.*;
import com.linkFlow.manager.common.service.LfUserService;
import com.linkFlow.manager.common.service.TokenInfoService;
import com.linkFlow.manager.common.service.TokenPointService;
import com.test32.common.paging.PagingData;
import com.test32.common.paging.PagingManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LfUserServiceImpl implements LfUserService
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired private LfUserDao lfUserDao;
    @Autowired private TokenInfoService tokenInfoService;
    @Autowired private TokenPointService tokenPointService;

    @Override public Integer countBySearch(Map<String, Object> parameter) { return lfUserDao.countBySearch(parameter); }
    @Override public boolean update(LfUserVO entity) { return lfUserDao.update(entity); }
    @Override public boolean delete(Integer index) { return lfUserDao.deleteByIndex(index); }
    @Override public boolean insert(LfUserVO entity) { return lfUserDao.insert(entity); }
    @Override public LfUserVO select(Integer index) { return lfUserDao.selectByIndex(index); }
    @Override public List<LfUserVO> selectAll() { return lfUserDao.selectAll(); }
    @Override public List<LfUserVO> selectBySearch(Map<String, Object> parameter) { return lfUserDao.selectBySearch(parameter); }

    @Override
    public PagingData selectPagingBySearch(Map<String, Object> parameter)
    {
        String pageNumberStr = (String) parameter.get("pageNo");
        String pageSizeStr = (String) parameter.get("pageSize");
        int pageNumber = StringUtils.isEmpty(pageNumberStr) ? 1 : Integer.valueOf(pageNumberStr);
        int pageSize = StringUtils.isEmpty(pageSizeStr) ? 20 : Integer.valueOf(pageSizeStr);
        int totalRecode = lfUserDao.countBySearch(parameter);

        PagingData pagingData = PagingManager.getPagingList(pageNumber, Long.valueOf("" + totalRecode), pageSize, 10);
        parameter.put("startRow", pagingData.getStartRow());
        parameter.put("rowCount", pagingData.getPageSize());

        List<LfUserVO> dataList = selectBySearch(parameter);
        pagingData.setObject(dataList);
        pagingData.setCurrentPageRowCount(dataList.size());

        return pagingData;
    }

    @Override
    public List<LfUserVOForApi> selectBySearchForApi(Map<String, Object> parameter)
    {
        return lfUserDao.selectBySearchForApi(parameter);
    }

    @Override
    public LfUserVOForSum selectActiveSum(Long mbIdx)
    {
        Map<String, Object> parameter = new HashMap<>();
        if(mbIdx != null)
            parameter.put("luMbIdx", mbIdx);
        parameter.put("luState", LfUserService.STATE_ACTIVE);
        return lfUserDao.selectSumBySearch(parameter);
    }

    @Override
    @Transactional("apiTransactionManager")
    public boolean expireItem(LfUserVO lfUserVO)
    {
        try
        {
            if(lfUserVO.getLuState() != LfUserService.STATE_ACTIVE)
            {
                logger.error("invalid state: " + lfUserVO);
                return false;
            }
            logger.info("lfUser.expire: " + lfUserVO);
            LfUserVO forUpdate = new LfUserVO();
            forUpdate.setLuIdx(lfUserVO.getLuIdx());
            forUpdate.setLuState(LfUserService.STATE_EXPIRED);

            TokenInfoVO tokenInfoVO = tokenInfoService.selectLfToken();
            TokenPointVO tokenPointVO = tokenPointService.selectByMemberTokenIdxOrCreate(lfUserVO.getLuMbIdx(), tokenInfoVO.getTkIdx(), true);

            boolean isTpSuccess = tokenPointService.addPoint(tokenPointVO, lfUserVO.getLuBaseAmount());
            boolean isLuSuccess = update(forUpdate);
            if(isTpSuccess && isLuSuccess)
                return true;

            logger.error("isTpSuccess: " + isTpSuccess);
            logger.error("isLuSuccess: " + isLuSuccess);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        catch (Exception e)
        {
            logger.error("" + lfUserVO);
            logger.error(e.toString());
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return false;
    }
}