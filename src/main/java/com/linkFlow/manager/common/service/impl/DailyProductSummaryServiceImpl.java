package com.linkFlow.manager.common.service.impl;

import com.linkFlow.manager.common.config.CommonConstants;
import com.linkFlow.manager.common.dao.DailyProductSummaryDao;
import com.linkFlow.manager.common.model.vo.*;
import com.linkFlow.manager.common.service.DailyProductSummaryService;
import com.linkFlow.manager.common.service.ProductInfoService;
import com.linkFlow.manager.common.service.ProductUserService;
import com.linkFlow.manager.common.service.TokenInfoService;
import com.linkFlow.manager.common.util.CustomComponentUtil;
import com.test32.common.paging.PagingData;
import com.test32.common.paging.PagingManager;
import com.test32.common.util.CommonDateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DailyProductSummaryServiceImpl implements DailyProductSummaryService
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired private DailyProductSummaryDao dailyProductSummaryDao;
    @Autowired private CustomComponentUtil customComponentUtil;
    @Autowired private ProductUserService productUserService;
    @Autowired private TokenInfoService tokenInfoService;
    @Autowired private ProductInfoService productInfoService;

    @Override public boolean insert(DailyProductSummaryVO entity) { return dailyProductSummaryDao.insert(entity); }
    @Override public DailyProductSummaryVO select(Integer index) { return dailyProductSummaryDao.selectByIndex(index);}
    @Override public List<DailyProductSummaryVO> selectAll() { return dailyProductSummaryDao.selectAll(); }
    @Override public List<DailyProductSummaryVO> selectBySearch(Map<String, Object> parameter) { return dailyProductSummaryDao.selectBySearch(parameter); }
    @Override public boolean update(DailyProductSummaryVO entity) { return dailyProductSummaryDao.update(entity); }
    @Override public boolean delete(Integer index) { return dailyProductSummaryDao.deleteByIndex(index); }

    @Override
    public boolean insertOrUpdate(DailyProductSummaryVO entity)
    {
        if(dailyProductSummaryDao.updateByKey(entity))
            return true;
        else
            return dailyProductSummaryDao.insert(entity);
    }

    @Override
    public PagingData selectPagingBySearch(Map<String, Object> parameter)
    {
        String pageNumberStr = (String) parameter.get("pageNo");
        String pageSizeStr = (String) parameter.get("pageSize");
        int pageNumber = StringUtils.isEmpty(pageNumberStr) ? 1 : Integer.parseInt(pageNumberStr);
        int pageSize = StringUtils.isEmpty(pageSizeStr) ? 20 : Integer.parseInt(pageSizeStr);
        Integer totalRecode = dailyProductSummaryDao.countBySearch(parameter);

        if(parameter.containsKey("searchStartDate"))
        {
            String data = (String) parameter.get("searchStartDate");
            if(data.length() > 10)
                parameter.put("searchStartDate", data.substring(0, 10));
        }
        if(parameter.containsKey("searchEndDate"))
        {
            String data = (String) parameter.get("searchEndDate");
            if(data.length() > 10)
                parameter.put("searchEndDate", data.substring(0, 10));
        }

        PagingData pagingData = PagingManager.getPagingList(pageNumber, Long.valueOf(totalRecode), pageSize, 10);
        parameter.put("startRow", pagingData.getStartRow());
        parameter.put("rowCount", pagingData.getPageSize());

        List<DailyProductSummaryVO> dataList = selectBySearch(parameter);
        pagingData.setObject(dataList);
        pagingData.setCurrentPageRowCount(dataList.size());

        return pagingData;
    }

    @Override
    public void runDaily(Date targetDate, boolean isFixed)
    {
        List<TokenInfoVO> list = tokenInfoService.selectAll();
        for(TokenInfoVO item : list)
        {
            switch (item.getTkSymbol())
            {
                case TokenInfoService.DEF_TOKEN_USDT:
                case TokenInfoService.DEF_TOKEN_USDC:
                {
                    runByProductInfo(targetDate, item, isFixed);
                    break;
                }
            }
        }
    }

    @Override
    public void runYesterday()
    {
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = sdf.format(CommonDateUtil.addDay(customComponentUtil.getDatabaseNow(), -1));
            Map<String, Object> parameter = new HashMap<>();
            parameter.put("searchEndDate", dateString);
            parameter.put("dsFlagFix", 0);

            List<Date> targetDateList = new ArrayList<>();
            List<DailyProductSummaryVO> list = selectBySearch(parameter);
            for(DailyProductSummaryVO item : list)
            {
                Date targetDate = sdf.parse(item.getDsDate());
                if( ! targetDateList.contains(targetDate))
                    targetDateList.add(targetDate);
            }
            for(Date targetDate : targetDateList)
                runDaily(targetDate, true);
        }
        catch (Exception e)
        {
            logger.error(e.toString());
            e.printStackTrace();
        }
    }

    private DailyProductSummaryVO generateDefaultEntity(Date targetDate, TokenInfoVO tokenInfoVO)
    {
        Date dayStart = CommonDateUtil.getDayStart(targetDate, CommonConstants.CONFIG_TIME_ZONE);
        SimpleDateFormat resultFormat = customComponentUtil.newSimpleDateFormatFull();
        String fullString = resultFormat.format(dayStart);
        String yearString = fullString.substring(0, 4);
        String monthString = fullString.substring(0, 7);
        String dateString = fullString.substring(0, 10);

        DailyProductSummaryVO entity = new DailyProductSummaryVO();
        entity.setDsYear(yearString);
        entity.setDsMonth(monthString);
        entity.setDsDate(dateString);

        entity.setDsTkIdx(tokenInfoVO.getTkIdx());
        entity.setDsTkSymbol(tokenInfoVO.getTkSymbol());

        return entity;
    }

    private String getStartDateString(Date targetDate, SimpleDateFormat resultFormat) { return resultFormat.format(CommonDateUtil.getDayStart(targetDate, CommonConstants.CONFIG_TIME_ZONE)); }
    private String getEndDateString(Date targetDate, SimpleDateFormat resultFormat) { return resultFormat.format(CommonDateUtil.getDayEnd(targetDate, CommonConstants.CONFIG_TIME_ZONE)); }


    private void runByProductInfo(Date targetDate, TokenInfoVO tokenInfoVO, boolean isFixed)
    {
        try
        {
            DailyProductSummaryVO entity = generateDefaultEntity(targetDate, tokenInfoVO);

            SimpleDateFormat resultFormat = customComponentUtil.newSimpleDateFormatFull();
            String startDateString = getStartDateString(targetDate, resultFormat);
            String endDateString = getEndDateString(targetDate, resultFormat);

            {
                BigDecimal newDepositAmount = BigDecimal.ZERO;
                Map<String, Object> parameter = new HashMap<>();
                parameter.put("puCreateDateOver", startDateString);
                parameter.put("puCreateDateUnder", endDateString);
                parameter.put("puTkIdx", tokenInfoVO.getTkIdx());

                List<ProductUserVO> list = productUserService.selectBySearch(parameter);
                for(ProductUserVO item : list)
                    newDepositAmount = newDepositAmount.add(item.getPuBaseAmount());

                entity.setDsCountDeposit(list.size());
                entity.setDsSumDeposit(newDepositAmount);
            }
            {
                BigDecimal totalDepositAmount = BigDecimal.ZERO;

                Map<String, Object> parameter = new HashMap<>();
                parameter.put("puTkIdx", tokenInfoVO.getTkIdx());
                List<ProductUserVO> list = productUserService.selectBySearch(parameter);
                for(ProductUserVO item : list)
                {
                    totalDepositAmount = totalDepositAmount.add(item.getPuBaseAmount());
                }
                entity.setDsSumAccumulatedBase(totalDepositAmount);

            }
            {
                BigDecimal totalInterestAmount = BigDecimal.ZERO;

                Map<String, Object> parameter = new HashMap<>();
                parameter.put("pdTkIdx", tokenInfoVO.getTkIdx());
                List<ProductInfoVO> list = productInfoService.selectBySearch(parameter);
                for(ProductInfoVO item : list)
                {
                    if(item.getPdCurrentInterest() != null)
                        totalInterestAmount = totalInterestAmount.add(item.getPdCurrentInterest());
                }
                entity.setDsSumAccumulatedInterest(totalInterestAmount);
            }

            entity.setDsSumAccumulatedTotal(entity.getDsSumAccumulatedBase().add(entity.getDsSumAccumulatedInterest()));
            if(isFixed)
                entity.setDsFlagFix(1);
            insertOrUpdate(entity);
        }
        catch (Exception e)
        {
            logger.error(e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public List<DailyProductSummaryVOForApi> selectBySearchForApi(Map<String, Object> parameter)
    {
        return dailyProductSummaryDao.selectBySearchForApi(parameter);
    }
}