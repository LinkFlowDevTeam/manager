package com.linkFlow.manager.common.service.impl;

import com.linkFlow.manager.common.config.CommonConstants;
import com.linkFlow.manager.common.config.ConfigDataManager;
import com.linkFlow.manager.common.dao.MemberDao;
import com.linkFlow.manager.common.model.vo.*;
import com.linkFlow.manager.common.service.*;
import com.linkFlow.manager.common.util.CustomComponentUtil;
import com.test32.common.paging.PagingData;
import com.test32.common.paging.PagingManager;
import com.test32.common.util.CommonDateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional("apiTransactionManager")
public class MemberServiceImpl implements MemberService
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired private CustomComponentUtil customComponentUtil;
    @Autowired private ConfigDataManager configDataManager;
    @Autowired private MemberDao memberDao;
    @Autowired private MemberTierService memberTierService;
    @Autowired private LfUserService lfUserService;
    @Autowired private TokenPointService tokenPointService;
    @Autowired private TokenInfoService tokenInfoService;

    @Override public MemberVO select(Long index) { return memberDao.selectByIndex(index); }
    @Override public List<MemberVO> selectAll() { return memberDao.selectAll(); }
    @Override public List<MemberVO> selectBySearch(Map<String, Object> parameter) { return memberDao.selectBySearch(parameter); }
    @Override public long countBySearch(Map<String, Object> parameter) { return memberDao.countBySearch(parameter); }
    @Override public boolean update(MemberVO entity) { return memberDao.update(entity); }
    @Override public boolean delete(Long index) { return memberDao.deleteByIndex(index); }

    @Override public boolean increaseErrorCount(MemberVO memberVO) { Integer maxUserErrorCount = configDataManager.getConfigData().getDefaultServerConfigData().getMaxUserError(); Map<String, Object> parameter = new HashMap<>(); parameter.put("mbIdx", memberVO.getMbIdx()); parameter.put("maxErrorCount", maxUserErrorCount); return memberDao.increaseErrorCount(parameter); }
    @Override public boolean decreaseErrorCount(MemberVO memberVO) { Map<String, Object> parameter = new HashMap<>(); parameter.put("mbIdx", memberVO.getMbIdx()); return memberDao.decreaseErrorCount(parameter); }
    @Override public boolean resetErrorCount(MemberVO memberVO) { Map<String, Object> parameter = new HashMap<>(); parameter.put("mbIdx", memberVO.getMbIdx()); return memberDao.resetErrorCount(parameter); }

    @Override public boolean insert(MemberVO entity) { return memberDao.insert(entity); }

    @Override public MemberVOForApi selectForApi(long idx) { Map<String, Object> searchMap = new HashMap<>(); searchMap.put("mbIdx", idx); List<MemberVOForApi> members = memberDao.selectBySearchForApi(searchMap); if (members.size() == 1) return members.get(0); return null; }
    @Override public MemberVO selectById(String id) { Map<String, Object> searchMap = new HashMap<>(); searchMap.put("mbId", id); List<MemberVO> members = memberDao.selectBySearch(searchMap); if (members != null && members.size() == 1) return members.get(0); return null; }

    @Override
    public void refreshTierAll()
    {
        TokenInfoVO lfTokenInfoVO = tokenInfoService.selectLfToken();

        Map<String, Object> tpMap = new HashMap<>();
        tpMap.put("tpTkIdx", lfTokenInfoVO.getTkIdx());
        TokenPointVOForSum tpTotalSum = tokenPointService.selectBySearchForSum(tpMap);

        LfUserVOForSum totalLfUserVOForSum = lfUserService.selectActiveSum(null);

        BigDecimal totalLfDeposit = BigDecimal.ZERO;
        if(tpTotalSum != null && tpTotalSum.getTpPoint() != null)
            totalLfDeposit = totalLfDeposit.add(tpTotalSum.getTpPoint());
        if(totalLfUserVOForSum.getAmount() != null)
            totalLfDeposit = totalLfDeposit.add(totalLfUserVOForSum.getAmount());

        List<MemberVO> list = selectAll();
        for(MemberVO item : list)
        {
            long mbIdx = item.getMbIdx();
            tpMap.put("tpTkIdx", lfTokenInfoVO.getTkIdx());
            tpMap.put("tpMbIdx", mbIdx);
            TokenPointVOForSum tpMySum = tokenPointService.selectBySearchForSum(tpMap);
            LfUserVOForSum myLfUserVOForSum = lfUserService.selectActiveSum(mbIdx);
            BigDecimal myLfDeposit = BigDecimal.ZERO;
            if(tpMySum != null && tpMySum.getTpPoint() != null)
                myLfDeposit = myLfDeposit.add(tpMySum.getTpPoint());
            if(myLfUserVOForSum.getAmount() != null)
                myLfDeposit = myLfDeposit.add(myLfUserVOForSum.getAmount());

            BigDecimal rate = myLfDeposit.setScale(4, RoundingMode.HALF_UP).divide(totalLfDeposit, RoundingMode.HALF_UP).setScale(2, RoundingMode.FLOOR);
            BigDecimal convertedRate = new BigDecimal("1").subtract(rate).multiply(new BigDecimal("100"));

            logger.info(mbIdx +  " rate: " + convertedRate.toPlainString());
            logger.info(myLfDeposit.toPlainString() + " / " + totalLfDeposit.toPlainString());
            MemberTierVO memberTierVO = memberTierService.selectByRate(convertedRate);

            MemberVO forUpdate = new MemberVO();
            forUpdate.setMbIdx(mbIdx);
            forUpdate.setMbTier(memberTierVO.getMtTier());
            forUpdate.setMbRate(convertedRate.setScale(0, RoundingMode.FLOOR).toPlainString());

            if(myLfDeposit.compareTo(BigDecimal.ZERO) <= 0)
                forUpdate.setMbTier(0);
            update(forUpdate);
        }
    }

    @Override
    public boolean refreshLfTokenLocked(long mbIdx)
    {
        TokenInfoVO lfTokenInfoVO = tokenInfoService.selectLfToken();
        LfUserVOForSum lfUserVOForSum = lfUserService.selectActiveSum(mbIdx);
        BigDecimal sumLf = lfUserVOForSum.getAmount();

        TokenPointVO tokenPointVO = tokenPointService.selectByMemberTokenIdxOrCreate(mbIdx, lfTokenInfoVO.getTkIdx(), true);
        return tokenPointService.overwriteLockedPoint(tokenPointVO, sumLf);
    }

    @Override
    public PagingData selectPagingBySearch(Map<String, Object> parameter)
    {
        String pageNumberStr = (String) parameter.get("pageNo");
        int pageNumber = pageNumberStr.equals("null") ? 1 : Integer.valueOf(pageNumberStr);
        Long totalRecode = memberDao.countBySearch(parameter);
        int pageSize = Integer.valueOf((String) parameter.get("pageSize"));

        PagingData pagingData = PagingManager.getPagingList(pageNumber, totalRecode, pageSize, 10);
        parameter.put("startRow", pagingData.getStartRow());
        parameter.put("rowCount", pagingData.getPageSize());

        List<MemberVO> dataList = selectBySearch(parameter);
        pagingData.setObject(dataList);
        pagingData.setCurrentPageRowCount(dataList.size());

        return pagingData;
    }

    @Override
    public MemberVOForApi selectBySearchForApi(Map<String, Object> searchMap)
    {
        List<MemberVOForApi> list = memberDao.selectBySearchForApi(searchMap);
        if(list.size() == 1)
        {
            return list.get(0);
        }
        if(list.size() > 1)
        {
            logger.error("invalid search result.  size: " + list.size());
        }
        return null;
    }


    @Override
    public long countNewUser(Date targetDate)
    {
        SimpleDateFormat sdf = customComponentUtil.newSimpleDateFormatFull();
        String startDateString = sdf.format(CommonDateUtil.getDayStart(targetDate, CommonConstants.CONFIG_TIME_ZONE));
        String endDateString = sdf.format(CommonDateUtil.getDayEnd(targetDate, CommonConstants.CONFIG_TIME_ZONE));

        Map<String, Object> parameter = new HashMap<>();
        parameter.put("mbCreateDateOver", startDateString);
        parameter.put("mbCreateDateUnder", endDateString);
        return countBySearch(parameter);
    }
}