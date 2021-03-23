package com.linkFlow.manager.common.service.impl;

import com.linkFlow.manager.common.config.ConfigDataManager;
import com.linkFlow.manager.common.dao.TokenInfoDao;
import com.linkFlow.manager.common.model.BaseResponse;
import com.linkFlow.manager.common.model.ReturnCode;
import com.linkFlow.manager.common.model.vo.TokenChartVOForApi;
import com.linkFlow.manager.common.model.vo.TokenInfoVO;
import com.linkFlow.manager.common.model.vo.TokenInfoVOForApi;
import com.linkFlow.manager.common.model.vo.TokenPriceVOForApi;
import com.linkFlow.manager.common.service.TokenInfoService;
import com.linkFlow.manager.common.util.CustomComponentUtil;
import com.linkFlow.manager.common.util.CustomErc20Util;
import com.test32.common.model.blockChain.erc20.jsonRpc.JsonRpcBalanceOf;
import com.test32.common.paging.PagingData;
import com.test32.common.paging.PagingManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TokenInfoServiceImpl implements TokenInfoService
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired private CustomComponentUtil customComponentUtil;
    @Autowired private ConfigDataManager configDataManager;
    @Autowired private CustomErc20Util customErc20Util;
    @Autowired private TokenInfoDao tokenInfoDao;

    @Override public boolean insert(TokenInfoVO entity) { return tokenInfoDao.insert(entity); }
    @Override public TokenInfoVO select(Integer index) { return tokenInfoDao.selectByIndex(index); }
    @Override public List<TokenInfoVO> selectAll() { return tokenInfoDao.selectAll(); }
    @Override public List<TokenInfoVO> selectBySearch(Map<String, Object> parameter) { return tokenInfoDao.selectBySearch(parameter); }
    @Override public boolean update(TokenInfoVO entity) { return tokenInfoDao.update(entity); }
    @Override public boolean delete(Integer index) { return tokenInfoDao.deleteByIndex(index); }

    @Override
    public String substringIfRequired(String input)
    {
        if(! StringUtils.isEmpty(input))
        {
            if(input.length() > TokenInfoService.MAX_TICKER_VALUE_LENGTH)
                return input.substring(0, 20);
            return input;
        }
        return null;
    }

    @Override
    public List<TokenInfoVO> selectDefaultGenerationList()
    {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("tkDefaultGeneration", TokenInfoService.DEFAULT_GENERATION_ON);
        return tokenInfoDao.selectBySearch(parameter);
    }

    @Override
    public List<TokenInfoVO> selectEnabledList()
    {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("tkState", TokenInfoService.TOKEN_STATE_ENABLE);
        return tokenInfoDao.selectBySearch(parameter);
    }

    @Override
    public List<TokenInfoVO> selectTkAddressNotNull()
    {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("tkAddressIsNotNull", "tkAddressIsNotNull");
        return tokenInfoDao.selectBySearch(parameter);
    }

    @Override
    public TokenInfoVO selectLfToken()
    {
        return selectBySymbol(TokenInfoService.DEF_TOKEN_LF);
    }

    @Override
    public TokenInfoVO selectBySymbol(String symbol)
    {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("tkSymbol", symbol);
        List<TokenInfoVO> list = tokenInfoDao.selectBySearch(parameter);
        if(list.size() == 1)
            return list.get(0);
        return null;
    }

    @Override
    public TokenInfoVO selectByErc20Address(String address)
    {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("tkType", TokenInfoService.TOKEN_TYPE_ERC20);
        parameter.put("tkAddress", address);
        List<TokenInfoVO> list = tokenInfoDao.selectBySearch(parameter);
        if(list.size() == 1)
            return list.get(0);
        return null;
    }

    @Override
    public BaseResponse refreshErc20Point(Integer tkIdx)
    {
        BaseResponse response = new BaseResponse();
        ReturnCode returnCode = ReturnCode.INTERNAL_ERROR;
        String extraMessage = null;
        try
        {
            {
                String withdrawAccount = configDataManager.getConfigData().getDefaultServerConfigData().getErc20WithdrawAccount();

                TokenInfoVO tokenInfoVO = select(tkIdx);
                {
                    {
                        Date now = customComponentUtil.getDatabaseNow();

                        // 주소 있을때만
                        if( ! StringUtils.isEmpty(tokenInfoVO.getTkAddress()))
                        {
                            JsonRpcBalanceOf jsonRpcBalanceOf = customErc20Util.balanceOf(tokenInfoVO.getTkAddress(), withdrawAccount);
                            if(jsonRpcBalanceOf == null)
                            {
                                returnCode = ReturnCode.ERC20_API_ERROR;
                                extraMessage = "response is null";
                            }
                            else
                            {
                                if(StringUtils.isEmpty(jsonRpcBalanceOf.getResult()))
                                {
                                    returnCode = ReturnCode.ERC20_API_ERROR;
                                    extraMessage = "getResult is null";
                                }
                                else
                                {
                                    if("0x".equalsIgnoreCase(jsonRpcBalanceOf.getResult()))
                                    {
                                        logger.info("no balance");
                                        returnCode = ReturnCode.SUCCESS;
                                    }
                                    else
                                    {
                                        //BigDecimal amount = customErc20Util.convertHexToDecimal(jsonRpcBalanceOf.getResult()).multiply(CommonConstants.ERC20_FLOATING_MULTIPLIER);
                                        BigDecimal amount = customErc20Util.convertHexToDecimalWithTokenInfo(jsonRpcBalanceOf.getResult(), tokenInfoVO.getTkDecimal());
                                        logger.info(jsonRpcBalanceOf.getResult() + "  " + amount);

                                        TokenInfoVO forUpdate = new TokenInfoVO();
                                        forUpdate.setTkIdx(tkIdx);
                                        forUpdate.setTkErc20Point(amount);
                                        forUpdate.setTkErc20Date(now);
                                        forUpdate.setTkErc20State(TokenInfoService.ERC20_STATE_NONE);
                                        update(forUpdate);

                                        returnCode = ReturnCode.SUCCESS;
                                    }
                                }
                            }
                        }

                        {
                            JsonRpcBalanceOf jsonRpcBalanceOf = customErc20Util.ethBalance(withdrawAccount);
                            if(jsonRpcBalanceOf == null)
                            {
                                returnCode = ReturnCode.ERC20_API_ERROR;
                                extraMessage = "response is null";
                            }
                            else
                            {
                                if(StringUtils.isEmpty(jsonRpcBalanceOf.getResult()))
                                {
                                    returnCode = ReturnCode.ERC20_API_ERROR;
                                    extraMessage = "getResult is null";
                                }
                                else
                                {
                                    if("0x".equalsIgnoreCase(jsonRpcBalanceOf.getResult()))
                                    {
                                        logger.info("no balance");
                                        returnCode = ReturnCode.SUCCESS;
                                    }
                                    else
                                    {
                                        BigDecimal amount = customErc20Util.convertHexToDecimalWithTokenInfo(jsonRpcBalanceOf.getResult(), 18);
                                        logger.info(jsonRpcBalanceOf.getResult() + "  " + amount);

                                        returnCode = ReturnCode.SUCCESS;
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }
        catch (Exception e)
        {
            logger.error(e.toString());
        }
        response.setReturnCode(returnCode);
        response.setDescription(response.getReturnCode().getMessage());
        response.setExtraMessage(extraMessage);
        return response;
    }

    @Override
    public PagingData selectPagingBySearch(Map<String, Object> parameter)
    {
        String pageNumberStr = (String) parameter.get("pageNo");
        String pageSizeStr = (String) parameter.get("pageSize");
        int pageNumber = StringUtils.isEmpty(pageNumberStr) ? 1 : Integer.valueOf(pageNumberStr);
        int pageSize = StringUtils.isEmpty(pageSizeStr) ? 20 : Integer.valueOf(pageSizeStr);
        int totalRecode = tokenInfoDao.countBySearch(parameter);

        PagingData pagingData = PagingManager.getPagingList(pageNumber, Long.valueOf("" + totalRecode), pageSize, 10);
        parameter.put("startRow", pagingData.getStartRow());
        parameter.put("rowCount", pagingData.getPageSize());

        List<TokenInfoVO> dataList = selectBySearch(parameter);
        pagingData.setObject(dataList);
        pagingData.setCurrentPageRowCount(dataList.size());

        return pagingData;
    }

    @Override
    public List<TokenInfoVOForApi> selectBySearchForInfoApi(String symbol)
    {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("tkState", TokenInfoService.TOKEN_STATE_ENABLE);
        parameter.put("orderColumn", "tk_order");
        if( ! StringUtils.isEmpty(symbol))
            parameter.put("tkSymbol", symbol);
        return tokenInfoDao.selectBySearchForInfoApi(parameter);
    }

    @Override
    public List<TokenPriceVOForApi> selectBySearchForPriceApi(String symbol)
    {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("tkState", TokenInfoService.TOKEN_STATE_ENABLE);
        if( ! StringUtils.isEmpty(symbol))
            parameter.put("tkSymbol", symbol);
        return tokenInfoDao.selectBySearchForPriceApi(parameter);
    }

    @Override
    public List<TokenChartVOForApi> selectBySearchForChartApi(String symbol)
    {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("tkState", TokenInfoService.TOKEN_STATE_ENABLE);
        if( ! StringUtils.isEmpty(symbol))
            parameter.put("tkSymbol", symbol);
        return tokenInfoDao.selectBySearchForChartApi(parameter);
    }

    @Override
    public boolean refreshTokenPrice(Integer tokenIdx)
    {
        return refreshTokenPrice(select(tokenIdx));
    }

    @Override
    public boolean refreshTokenPrice(TokenInfoVO tokenInfoVO)
    {
        switch (tokenInfoVO.getTkMarketName())
        {
            default:
                logger.error("out of case.  no config for: " + tokenInfoVO.getTkSymbol());
                return false;
        }
    }
}