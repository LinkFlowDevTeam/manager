package com.linkFlow.manager.common.service.impl;

import com.linkFlow.manager.common.dao.CommonWorkQueueDao;
import com.linkFlow.manager.common.model.BaseResponse;
import com.linkFlow.manager.common.model.ReturnCode;
import com.linkFlow.manager.common.model.vo.CommonWorkQueueVO;
import com.linkFlow.manager.common.model.vo.MemberVO;
import com.linkFlow.manager.common.model.vo.TokenInfoVO;
import com.linkFlow.manager.common.model.vo.TransferManagerQueueVO;
import com.linkFlow.manager.common.service.*;
import com.test32.common.paging.PagingData;
import com.test32.common.paging.PagingManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("commonWorkQueueService")
public class CommonWorkQueueServiceImpl implements CommonWorkQueueService
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired private CommonWorkQueueDao commonWorkQueueDao;
    @Autowired private MemberService memberService;
    @Autowired private TransferManagerQueueService transferManagerQueueService;
    @Autowired private TokenInfoService tokenInfoService;
    @Autowired private TokenPointService tokenPointService;

    @Override public boolean insert(CommonWorkQueueVO entity) { return commonWorkQueueDao.insert(entity); }
    @Override public CommonWorkQueueVO select(Long index) { return commonWorkQueueDao.selectByIndex(index); }
    @Override public List<CommonWorkQueueVO> selectAll() { return commonWorkQueueDao.selectAll(); }
    @Override public List<CommonWorkQueueVO> selectBySearch(Map<String, Object> parameter) { return commonWorkQueueDao.selectBySearch(parameter); }
    @Override public boolean update(CommonWorkQueueVO entity) { return commonWorkQueueDao.update(entity); }
    @Override public boolean delete(Long index) { return commonWorkQueueDao.deleteByIndex(index); }

    @Override
    public PagingData selectPagingBySearch(Map<String, Object> parameter)
    {
        String pageNumberStr = (String) parameter.get("pageNo");
        String pageSizeStr = (String) parameter.get("pageSize");
        int pageNumber = StringUtils.isEmpty(pageNumberStr) ? 1 : Integer.valueOf(pageNumberStr);
        int pageSize = StringUtils.isEmpty(pageSizeStr) ? 20 : Integer.valueOf(pageSizeStr);
        Long totalRecode = commonWorkQueueDao.countBySearch(parameter);

        PagingData pagingData = PagingManager.getPagingList(pageNumber, totalRecode, pageSize, 10);
        parameter.put("startRow", pagingData.getStartRow());
        parameter.put("rowCount", pagingData.getPageSize());

        List<CommonWorkQueueVO> dataList = selectBySearch(parameter);
        pagingData.setObject(dataList);
        pagingData.setCurrentPageRowCount(dataList.size());

        return pagingData;
    }

    @PostConstruct
    public boolean checkInitialData()
    {
        boolean result = false;
        try
        {
            // 오류인것 재시도
            Map<String, Object> parameter = new HashMap<>();
            parameter.put("wqState", CommonWorkQueueService.STATE_ERROR);
            parameter.put("wqExecuteMode", CommonWorkQueueService.EXECUTE_MODE_RUN_MULTIPLE_ABLE);
            List<CommonWorkQueueVO> list = selectBySearch(parameter);
            for(CommonWorkQueueVO item : list)
            {
                logger.warn("execute missing item: " + item);
                executeItem(item);
            }
            result = true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void runScheduledDefault()
    {
        //List<Integer> wqTypeList = new ArrayList<>();
        //wqTypeList.add(CommonWorkQueueService.TYPE_MEMBER_ERC20_REFRESH);
        //wqTypeList.add(CommonWorkQueueService.TYPE_MANAGER_ERC20_REFRESH);

        Map<String, Object> parameter = new HashMap<>();
        parameter.put("wqType", CommonWorkQueueService.TYPE_ERC20_REFRESH);
        parameter.put("wqState", CommonWorkQueueService.STATE_NONE);
        List<CommonWorkQueueVO> list = selectBySearch(parameter);
        for(CommonWorkQueueVO item : list)
        {
            logger.warn("execute scheduled item: " + item);
            executeItem(item);
        }
    }

    @Override
    public void runScheduledMemberStatistic()
    {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("wqType", CommonWorkQueueService.TYPE_MEMBER_SNAPSHOT);
        parameter.put("wqState", CommonWorkQueueService.STATE_NONE);
        List<CommonWorkQueueVO> list = selectBySearch(parameter);
        for(CommonWorkQueueVO item : list)
        {
            logger.warn("execute scheduled item: " + item);
            executeItem(item);
        }
    }

    private boolean markAsSuccess(CommonWorkQueueVO entity, String text)
    {
        CommonWorkQueueVO forUpdate = new CommonWorkQueueVO();
        forUpdate.setWqIdx(entity.getWqIdx());
        forUpdate.setWqState(CommonWorkQueueService.STATE_SUCCESS);
        forUpdate.setWqFinishDate(new Date());
        return update(forUpdate);
    }

    private boolean markAsError(CommonWorkQueueVO entity, String text)
    {
        CommonWorkQueueVO forUpdate = new CommonWorkQueueVO();
        forUpdate.setWqIdx(entity.getWqIdx());
        forUpdate.setWqState(CommonWorkQueueService.STATE_ERROR);
        forUpdate.setWqText(text);
        return update(forUpdate);
    }

    private boolean markAsCheckRequired(CommonWorkQueueVO entity, String text)
    {
        CommonWorkQueueVO forUpdate = new CommonWorkQueueVO();
        forUpdate.setWqIdx(entity.getWqIdx());
        forUpdate.setWqState(CommonWorkQueueService.STATE_CHECK_REQUIRED);
        forUpdate.setWqText(text);
        return update(forUpdate);
    }

    @Override
    public boolean executeItem(CommonWorkQueueVO entity)
    {
        switch (entity.getWqType())
        {
            case CommonWorkQueueService.TYPE_MEMBER_SNAPSHOT: return executeMemberSnapshot(entity);
            case CommonWorkQueueService.TYPE_ERC20_REFRESH: return executeErc20AddressTokenRefresh(entity);
            default:
                logger.error("type: " + entity.getWqType() + "  " + entity);
                break;
        }
        return false;
    }

    private boolean executeMemberSnapshot(CommonWorkQueueVO entity)
    {
        try
        {
            commonWorkQueueDao.updateStartDate(entity.getWqIdx());

            // 통계용 정보 업데이트
            MemberVO memberVO = memberService.select(entity.getWqTargetIdx());

            markAsSuccess(entity, null);
            return true;
        }
        catch (Exception e)
        {
            logger.error(e.toString());
            e.printStackTrace();

            try
            {
                markAsError(entity, e.toString());
            }
            catch (Exception subE)
            {
                logger.error(subE.toString());
                e.printStackTrace();
            }
        }
        return false;
    }

    private boolean executeErc20AddressTokenRefresh(CommonWorkQueueVO entity)
    {
        try
        {
            commonWorkQueueDao.updateStartDate(entity.getWqIdx());

            boolean isSuccess = false;
            String targetAddress = entity.getWqData1();
            String symbol = entity.getWqData2();
            TokenInfoVO tokenInfoVO = tokenInfoService.selectBySymbol(symbol);
            if(tokenInfoVO != null)
            {
                {
                    TransferManagerQueueVO transferManagerQueueVO = transferManagerQueueService.selectByAddress(targetAddress, symbol);
                    if(transferManagerQueueVO != null)
                    {
                        BaseResponse result = transferManagerQueueService.refreshErc20Point(transferManagerQueueVO.getTqIdx());
                        if(result.getReturnCode() == ReturnCode.SUCCESS)
                            isSuccess = true;
                        else
                        {
                            logger.error("transferManagerQueueService.refreshErc20Point: " + entity);
                            logger.error("" + result);
                        }
                    }
                }
            }

            if(isSuccess)
                markAsSuccess(entity, null);
            else
                markAsCheckRequired(entity, null);
            return true;
        }
        catch (Exception e)
        {
            logger.error(e.toString());
            e.printStackTrace();

            try
            {
                markAsError(entity, e.toString());
            }
            catch (Exception subE)
            {
                logger.error(subE.toString());
                e.printStackTrace();
            }
        }
        return false;
    }


    private CommonWorkQueueVO generateMemberSnapshot(Long mbIdx)
    {
        CommonWorkQueueVO entity = new CommonWorkQueueVO();
        entity.setWqType(CommonWorkQueueService.TYPE_MEMBER_SNAPSHOT);
        entity.setWqState(CommonWorkQueueService.STATE_NONE);
        entity.setWqExecuteMode(CommonWorkQueueService.EXECUTE_MODE_RUN_MULTIPLE_ABLE);

        entity.setWqTargetIdx(mbIdx);
        return entity;
    }

    private CommonWorkQueueVO generateErc20Refresh(String erc20Address, String symbol)
    {
        CommonWorkQueueVO entity = new CommonWorkQueueVO();
        entity.setWqType(CommonWorkQueueService.TYPE_ERC20_REFRESH);
        entity.setWqState(CommonWorkQueueService.STATE_NONE);
        entity.setWqExecuteMode(CommonWorkQueueService.EXECUTE_MODE_RUN_MULTIPLE_ABLE);

        entity.setWqData1(erc20Address);
        entity.setWqData2(symbol);
        return entity;
    }

    @Override
    public boolean insertMemberSnapshot(Long mbIdx)
    {
        return insert(generateMemberSnapshot(mbIdx));
    }

    @Override
    public boolean insertErc20AddressTokenRefresh(String erc20Address, String symbol)
    {
        return insert(generateErc20Refresh(erc20Address, symbol));
    }
}