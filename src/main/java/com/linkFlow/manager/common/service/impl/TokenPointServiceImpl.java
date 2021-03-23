package com.linkFlow.manager.common.service.impl;

import com.linkFlow.manager.admin.ServiceException;
import com.linkFlow.manager.common.dao.TokenPointDao;
import com.linkFlow.manager.common.model.ReturnCode;
import com.linkFlow.manager.common.model.vo.*;
import com.linkFlow.manager.common.service.MemberService;
import com.linkFlow.manager.common.service.TokenInfoService;
import com.linkFlow.manager.common.service.TokenPointService;
import com.linkFlow.manager.common.util.CustomComponentUtil;
import com.test32.common.paging.PagingData;
import com.test32.common.paging.PagingManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TokenPointServiceImpl implements TokenPointService
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired private CustomComponentUtil customComponentUtil;
    @Autowired private TokenPointDao tokenPointDao;
    @Autowired private TokenInfoService tokenInfoService;
    @Autowired private MemberService memberService;

    @Override public boolean insert(TokenPointVO entity) { return tokenPointDao.insert(entity); }
    @Override public Long countBySearch(Map<String, Object> parameter) { return tokenPointDao.countBySearch(parameter); }
    @Override public TokenPointVO select(Long index) { return tokenPointDao.selectByIndex(index); }
    @Override public List<TokenPointVO> selectAll() { return tokenPointDao.selectAll(); }
    @Override public List<TokenPointVO> selectBySearch(Map<String, Object> parameter) { return tokenPointDao.selectBySearch(parameter); }
    @Override public boolean update(TokenPointVO entity) { return tokenPointDao.update(entity); }
    @Override public boolean delete(Long index) { return tokenPointDao.deleteByIndex(index); }

    @Override public List<TokenPointVOForApi> selectBySearchForApi(Map<String, Object> parameter) { return tokenPointDao.selectBySearchForApi(parameter); }
    @Override public TokenPointVOForSum selectBySearchForSum(Map<String, Object> parameter) { return tokenPointDao.selectBySearchForSum(parameter); }
    @Override public List<TokenPointVOForSum> selectBySearchListForSum(Map<String, Object> parameter) { return tokenPointDao.selectBySearchListForSum(parameter); }

    @Override
    public PagingData selectPagingBySearch(Map<String, Object> parameter)
    {
        String pageNumberStr = (String) parameter.get("pageNo");
        String pageSizeStr = (String) parameter.get("pageSize");
        int pageNumber = StringUtils.isEmpty(pageNumberStr) ? 1 : Integer.valueOf(pageNumberStr);
        int pageSize = StringUtils.isEmpty(pageSizeStr) ? 20 : Integer.valueOf(pageSizeStr);
        Long totalRecode = tokenPointDao.countBySearch(parameter);

        PagingData pagingData = PagingManager.getPagingList(pageNumber, totalRecode, pageSize, 10);
        parameter.put("startRow", pagingData.getStartRow());
        parameter.put("rowCount", pagingData.getPageSize());

        List<TokenPointVO> dataList = selectBySearch(parameter);
        pagingData.setObject(dataList);
        pagingData.setCurrentPageRowCount(dataList.size());

        return pagingData;
    }

    @Override
    public boolean createNew(MemberVO memberVO, TokenInfoVO tokenInfoVO)
    {
        TokenPointVO tokenPointVO = new TokenPointVO();
        tokenPointVO.setTpMbIdx(memberVO.getMbIdx());
        tokenPointVO.setTpTkIdx(tokenInfoVO.getTkIdx());
        tokenPointVO.setTpTkSymbol(tokenInfoVO.getTkSymbol());
        //tokenPointVO.setTpTkType(tokenInfoVO.getTkType());

        //tokenPointVO.setTpWorldPoint(BigDecimal.ZERO);
        tokenPointVO.setTpPoint(BigDecimal.ZERO);
        return insert(tokenPointVO);
    }

    @Override
    public boolean isEnoughAmount(Long mbIdx, Integer tkIdx, BigDecimal reqAmount)
    {
        boolean isEnoughValue = false;
        TokenPointVO tokenPointVO = selectByMemberTokenIdx(mbIdx, tkIdx);
        if(tokenPointVO != null)
        {
            if(tokenPointVO.getTpPoint().compareTo(reqAmount) >= 0)
                isEnoughValue = true;
        }
        return isEnoughValue;
    }

    @Override
    public TokenPointVO selectByMemberTokenIdxOrCreate(Long mbIdx, Integer tokenIdx, boolean isCreateIfNotExist)
    {
        TokenPointVO tokenPointVO = selectByMemberTokenIdx(mbIdx, tokenIdx);
        if(tokenPointVO == null && isCreateIfNotExist)
        {
            MemberVO memberVO = memberService.select(mbIdx);
            TokenInfoVO tokenInfoVO = tokenInfoService.select(tokenIdx);
            createNew(memberVO, tokenInfoVO);
            tokenPointVO = selectByMemberTokenIdx(mbIdx, tokenIdx);
        }
        return tokenPointVO;
    }

    @Override
    public TokenPointVO selectByMemberTokenIdx(Long mbIdx, Integer tokenIdx)
    {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("tpMbIdx", mbIdx);
        parameter.put("tpTkIdx", tokenIdx);
        List<TokenPointVO> list = tokenPointDao.selectBySearch(parameter);
        if(list.size() == 1)
            return list.get(0);
        return null;
    }

    @Override
    public TokenPointVO selectByMemberTokenSymbol(Long mbIdx, String symbol)
    {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("tpMbIdx", mbIdx);
        if( ! StringUtils.isEmpty(symbol))
        {
            parameter.put("tpTkSymbol", symbol);
        }
        List<TokenPointVO> list = tokenPointDao.selectBySearch(parameter);
        if(list.size() == 1)
            return list.get(0);
        return null;
    }

    @Override
    public List<TokenPointVO> selectByMember(Long mbIdx)
    {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("tpMbIdx", mbIdx);
        return tokenPointDao.selectBySearch(parameter);
    }

    @Override
    public boolean addPoint(TokenPointVO target, BigDecimal amount)
    {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("tpIdx", target.getTpIdx());
        parameter.put("amount", amount);
        if (!tokenPointDao.addPoint(parameter)) {
            throw new ServiceException(ReturnCode.DATA_UPDATE_FAILED);
        }
        return true;
    }

    @Override
    public boolean subtractPoint(TokenPointVO target, BigDecimal amount)
    {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("tpIdx", target.getTpIdx());
        parameter.put("amount", amount);
        if (!tokenPointDao.subtractPoint(parameter)) {
            throw new ServiceException(ReturnCode.DATA_UPDATE_FAILED);
        }
        return true;
    }

    @Override
    public boolean subtractPointForced(TokenPointVO target, BigDecimal amount)
    {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("tpIdx", target.getTpIdx());
        parameter.put("amount", amount);
        if (!tokenPointDao.subtractPointForced(parameter)) {
            throw new ServiceException(ReturnCode.DATA_UPDATE_FAILED);
        }
        return true;
    }

    @Override
    public boolean overwriteLockedPoint(TokenPointVO target, BigDecimal amount)
    {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("tpIdx", target.getTpIdx());
        parameter.put("amount", amount);
        if (!tokenPointDao.overwriteLockedPoint(parameter)) {
            throw new ServiceException(ReturnCode.DATA_UPDATE_FAILED);
        }
        return true;
    }
}