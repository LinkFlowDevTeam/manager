package com.linkFlow.manager.common.service.impl;

import com.linkFlow.manager.common.config.CommonConstants;
import com.linkFlow.manager.common.dao.ProductInfoDao;
import com.linkFlow.manager.common.model.BaseResponse;
import com.linkFlow.manager.common.model.ReturnCode;
import com.linkFlow.manager.common.model.vo.*;
import com.linkFlow.manager.common.service.ProductInfoService;
import com.linkFlow.manager.common.service.ProductUserService;
import com.linkFlow.manager.common.service.TokenInfoService;
import com.linkFlow.manager.common.service.TransactionService;
import com.linkFlow.manager.common.util.CustomComponentUtil;
import com.linkFlow.manager.common.util.CustomErc20Util;
import com.test32.common.model.blockChain.erc20.jsonRpc.JsonRpcBalanceOf;
import com.test32.common.paging.PagingData;
import com.test32.common.paging.PagingManager;
import com.test32.common.util.CommonDateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductInfoServiceImpl implements ProductInfoService
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired private CustomComponentUtil customComponentUtil;
    @Autowired private CustomErc20Util customErc20Util;
    @Autowired private ProductInfoDao productInfoDao;
    @Autowired private ProductUserService productUserService;
    @Autowired private TransactionService transactionService;

    @Override public Integer countBySearch(Map<String, Object> parameter) { return productInfoDao.countBySearch(parameter); }
    @Override public boolean update(ProductInfoVO entity) { return productInfoDao.update(entity); }
    @Override public boolean delete(Integer index) { return productInfoDao.deleteByIndex(index); }
    @Override public boolean insert(ProductInfoVO entity) { return productInfoDao.insert(entity); }
    @Override public ProductInfoVO select(Integer index) { return productInfoDao.selectByIndex(index); }
    @Override public List<ProductInfoVO> selectAll() { return productInfoDao.selectAll(); }
    @Override public List<ProductInfoVO> selectBySearch(Map<String, Object> parameter) { return productInfoDao.selectBySearch(parameter); }

    @Override
    public ProductInfoVO selectByErc20Address(String address)
    {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("pdErc20Address", address);
        List<ProductInfoVO> list = selectBySearch(parameter);
        if(list.size() == 1)
            return list.get(0);
        return null;
    }

    @Override
    public PagingData selectPagingBySearch(Map<String, Object> parameter)
    {
        String pageNumberStr = (String) parameter.get("pageNo");
        String pageSizeStr = (String) parameter.get("pageSize");
        int pageNumber = StringUtils.isEmpty(pageNumberStr) ? 1 : Integer.valueOf(pageNumberStr);
        int pageSize = StringUtils.isEmpty(pageSizeStr) ? 20 : Integer.valueOf(pageSizeStr);
        int totalRecode = productInfoDao.countBySearch(parameter);

        PagingData pagingData = PagingManager.getPagingList(pageNumber, Long.valueOf("" + totalRecode), pageSize, 10);
        parameter.put("startRow", pagingData.getStartRow());
        parameter.put("rowCount", pagingData.getPageSize());

        List<ProductInfoVO> dataList = selectBySearch(parameter);
        pagingData.setObject(dataList);
        pagingData.setCurrentPageRowCount(dataList.size());

        return pagingData;
    }

    @Override
    public List<ProductInfoVOForApi> selectBySearchForApi(Map<String, Object> parameter)
    {
        return productInfoDao.selectBySearchForApi(parameter);
    }

    @Override
    public BaseResponse refreshErc20Point(Integer pdIdx)
    {
        BaseResponse response = new BaseResponse();
        ReturnCode returnCode = ReturnCode.INTERNAL_ERROR;
        String extraMessage = null;
        Boolean isApiError = false;
        try
        {
            ProductInfoVO productInfoVO = select(pdIdx);
            TokenInfoVO tokenInfoVO = productInfoVO.getTokenInfoVO();

            String targetAddress = productInfoVO.getPdErc20Address();
            {
                {
                    if(StringUtils.isEmpty(targetAddress))
                    {
                        returnCode = ReturnCode.ERC20_ADDRESS_MISSING;
                        extraMessage = "user address not given";
                    }
                    else
                    {
                        Date now = customComponentUtil.getDatabaseNow();
                        // 주소 있을때만
                        if( ! StringUtils.isEmpty(tokenInfoVO.getTkAddress()))
                        {
                            JsonRpcBalanceOf jsonRpcBalanceOf = customErc20Util.balanceOf(tokenInfoVO.getTkAddress(), targetAddress);
                            if(jsonRpcBalanceOf == null)
                            {
                                extraMessage = "response is null";
                                isApiError = true;
                            }
                            else
                            {
                                if(StringUtils.isEmpty(jsonRpcBalanceOf.getResult()))
                                {
                                    extraMessage = "getResult is null";
                                    isApiError = true;
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
                                        BigDecimal amount = customErc20Util.convertHexToDecimalWithTokenInfo(jsonRpcBalanceOf.getResult(), tokenInfoVO.getTkDecimal());
                                        logger.info(jsonRpcBalanceOf.getResult() + "  " + amount);

                                        ProductInfoVO forUpdate = new ProductInfoVO();
                                        forUpdate.setPdIdx(pdIdx);
                                        forUpdate.setPdErc20Point(amount);
                                        forUpdate.setPdErc20Cached(amount);
                                        forUpdate.setPdErc20CachedDate(now);
                                        forUpdate.setPdErc20State(TokenInfoService.ERC20_STATE_NONE);
                                        update(forUpdate);

                                        returnCode = ReturnCode.SUCCESS;
                                    }
                                }
                            }
                        }

                        {
                            JsonRpcBalanceOf jsonRpcBalanceOf = customErc20Util.ethBalance(targetAddress);
                            if(jsonRpcBalanceOf == null)
                            {
                                extraMessage = "response is null";
                                isApiError = true;
                            }
                            else
                            {
                                if(StringUtils.isEmpty(jsonRpcBalanceOf.getResult()))
                                {
                                    extraMessage = "getResult is null";
                                    isApiError = true;
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

                                        ProductInfoVO forUpdate = new ProductInfoVO();
                                        forUpdate.setPdIdx(pdIdx);
                                        forUpdate.setPdEthPoint(amount);
                                        update(forUpdate);

                                        returnCode = ReturnCode.SUCCESS;
                                    }
                                }
                            }
                        }

                        if(isApiError)
                        {
                            logger.info("overwrite return code to API ERROR");
                            returnCode = ReturnCode.ERC20_API_ERROR;
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            logger.error(e.toString());
            e.printStackTrace();
        }
        response.setReturnCode(returnCode);
        response.setDescription(response.getReturnCode().getMessage());
        response.setExtraMessage(extraMessage);
        return response;
    }

    @Override
    @Transactional("apiTransactionManager")
    public BaseResponse start(Integer pdIdx)
    {
        BaseResponse response = new BaseResponse();
        ReturnCode returnCode = ReturnCode.INTERNAL_ERROR;
        String extraMessage = null;
        try
        {
            ProductInfoVO productInfoVO = select(pdIdx);
            ProductUserVOForSum sum = productUserService.selectSumDepositAmount(null, pdIdx);
            if(sum.getAmount().compareTo(BigDecimal.ZERO) <= 0)
            {
                returnCode = ReturnCode.DATA_NOT_FOUND;
                extraMessage = "no user joined";
            }
            else if(productInfoVO.getPdState() == ProductInfoService.STATE_STARTED || productInfoVO.getPdState() == ProductInfoService.STATE_EXPIRED)
            {
                returnCode = ReturnCode.INVALID_PRIOR_STATE;
            }
            else
            {
                ProductInfoVO forUpdate = new ProductInfoVO();
                forUpdate.setPdIdx(pdIdx);
                forUpdate.setPdState(ProductInfoService.STATE_STARTED);
                forUpdate.setPdStartDate(customComponentUtil.getDatabaseNow());
                forUpdate.setPdInterestDate(forUpdate.getPdStartDate());

                if(update(forUpdate) && productUserService.startProduct(productInfoVO))
                {
                    returnCode = ReturnCode.SUCCESS;
                }
                else
                {
                    logger.error("failed to: productUserService.startProduct");
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
            }
        }
        catch (Exception e)
        {
            logger.error(e.toString());
            e.printStackTrace();
            extraMessage = e.toString();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        response.setReturnCode(returnCode);
        response.setDescription(response.getReturnCode().getMessage());
        response.setExtraMessage(extraMessage);
        return response;
    }

    @Override
    public boolean refreshBaseAmount(int pdIdx)
    {
        ProductUserVOForSum sum =  productUserService.selectSumDepositAmount(null, pdIdx);

        ProductInfoVO forUpdate = new ProductInfoVO();
        forUpdate.setPdIdx(pdIdx);
        forUpdate.setPdBaseAmount(sum.getAmount());

        return productInfoDao.update(forUpdate);
    }

    @Override
    public boolean appendInterestAmount(int pdIdx, BigDecimal amount)
    {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("pdIdx", pdIdx);
        parameter.put("amount", amount);
        return productInfoDao.appendInterestAmount(parameter);
    }

    @Override
    @Transactional("apiTransactionManager")
    public boolean runDefaultInterest(ProductInfoVO productInfoVO)
    {
        try
        {
            Date dbNow = customComponentUtil.getDatabaseNow();
            Date todayStart = CommonDateUtil.getDayStart(dbNow, CommonConstants.CONFIG_TIME_ZONE);

            Date createDayStart = CommonDateUtil.getDayStart(productInfoVO.getPdStartDate(), CommonConstants.CONFIG_TIME_ZONE);
            Date priorInterestDayStart = CommonDateUtil.getDayStart(productInfoVO.getPdInterestDate(), CommonConstants.CONFIG_TIME_ZONE);
            Date targetInterestDate = CommonDateUtil.addDay(priorInterestDayStart, 1);

            if(todayStart.getTime() < targetInterestDate.getTime())
            {
                logger.warn("todayStart.getTime() < targetInterestDate.getTime() " + productInfoVO);
                return false;
            }

            int interestIndex = CommonDateUtil.getDaysDifference(createDayStart, targetInterestDate);

            Map<String, Object> puMap = new HashMap<>();
            puMap.put("puPdIdx", productInfoVO.getPdIdx());
            List<ProductUserVO> list = productUserService.selectBySearch(puMap);


            int countSuccess = 0;
            BigDecimal sumDailyInterest = BigDecimal.ZERO;
            for(ProductUserVO item : list)
            {
                BigDecimal totalInterestAmount = item.getPuBaseAmount().setScale(4, RoundingMode.CEILING).multiply(new BigDecimal(productInfoVO.getPdInterestRate()).multiply(new BigDecimal("0.01")));
                BigDecimal dailyInterest = totalInterestAmount.divide(new BigDecimal("" + productInfoVO.getPdDay()), RoundingMode.CEILING).setScale(4, RoundingMode.CEILING);
                sumDailyInterest = sumDailyInterest.add(dailyInterest);

                boolean isAppendSuccess = productUserService.appendInterestAmount(item, dailyInterest);
                boolean isTxSuccess = transactionService.addDailyInterest(productInfoVO, item, dailyInterest, interestIndex);
                if(isAppendSuccess && isTxSuccess)
                {
                    countSuccess++;
                }
                else
                {
                    logger.error("failed: " + item);
                    logger.error("isAppendSuccess: " + isAppendSuccess);
                    logger.error("isTxSuccess: " + isTxSuccess);
                }
            }

            ProductInfoVO forUpdate = new ProductInfoVO();
            forUpdate.setPdIdx(productInfoVO.getPdIdx());
            forUpdate.setPdInterestDate(targetInterestDate);
            boolean isStateSuccess = update(forUpdate);

            boolean isPdInterestSuccess = appendInterestAmount(productInfoVO.getPdIdx(), sumDailyInterest);

            boolean isPuInterestSuccess = (countSuccess == list.size());

            if(isStateSuccess && isPdInterestSuccess && isPuInterestSuccess)
                return true;

            logger.error("isPdInterestSuccess: " + isPdInterestSuccess);
            logger.error("isStateSuccess: " + isStateSuccess);
            logger.error("isPuInterestSuccess: " + isPuInterestSuccess);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        catch (Exception e)
        {
            logger.error(e.toString());
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return false;
    }
}
