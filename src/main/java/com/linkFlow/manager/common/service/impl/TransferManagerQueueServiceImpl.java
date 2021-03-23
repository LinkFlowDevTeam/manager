package com.linkFlow.manager.common.service.impl;

import com.linkFlow.manager.common.dao.TransferManagerQueueDao;
import com.linkFlow.manager.common.model.BaseResponse;
import com.linkFlow.manager.common.model.ReturnCode;
import com.linkFlow.manager.common.model.vo.PaymentQueueVO;
import com.linkFlow.manager.common.model.vo.TokenInfoVO;
import com.linkFlow.manager.common.model.vo.TransferManagerQueueVO;
import com.linkFlow.manager.common.service.PaymentQueueService;
import com.linkFlow.manager.common.service.TokenInfoService;
import com.linkFlow.manager.common.service.TransferManagerQueueService;
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
public class TransferManagerQueueServiceImpl implements TransferManagerQueueService
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired private CustomComponentUtil customComponentUtil;
    @Autowired private CustomErc20Util customErc20Util;
    @Autowired private TransferManagerQueueDao transferManagerQueueDao;
    @Autowired private PaymentQueueService paymentQueueService;
    @Autowired private TokenInfoService tokenInfoService;

    @Override public boolean insert(TransferManagerQueueVO entity) { return transferManagerQueueDao.insert(entity); }
    @Override public TransferManagerQueueVO select(Integer index) { return transferManagerQueueDao.selectByIndex(index); }
    @Override public List<TransferManagerQueueVO> selectAll() { return transferManagerQueueDao.selectAll(); }
    @Override public List<TransferManagerQueueVO> selectBySearch(Map<String, Object> parameter) { return transferManagerQueueDao.selectBySearch(parameter); }
    @Override public boolean update(TransferManagerQueueVO entity) { return transferManagerQueueDao.update(entity); }
    @Override public boolean delete(Integer index) { return transferManagerQueueDao.deleteByIndex(index); }

    @Override
    public boolean isAddressExist(String address)
    {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("tqAddress", address);
        return (transferManagerQueueDao.countBySearch(parameter) > 0);
    }

    @Override
    public TransferManagerQueueVO selectByAddress(String address, String symbol)
    {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("tqAddress", address);
        parameter.put("tqTkSymbol", symbol);
        List<TransferManagerQueueVO> list = selectBySearch(parameter);
        if(list.size() > 0)
            return list.get(0);
        return null;
    }

    @Override
    public void checkPaymentQueueState()
    {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("tqTargetState", TransferManagerQueueService.TARGET_STATE_WORKING);
        List<TransferManagerQueueVO> list = selectBySearch(parameter);
        for(TransferManagerQueueVO item : list)
        {
            boolean isFinished = false;
            PaymentQueueVO paymentQueueVO = paymentQueueService.select(item.getTqTargetIdx());
            if(paymentQueueVO == null)
                isFinished = true;
            else
            {
                switch (paymentQueueVO.getPqState())
                {
                    case PaymentQueueService.STATE_TRANSFER_TX_PENDING:
                    case PaymentQueueService.STATE_TRANSFER_TX_MISSING:
                    case PaymentQueueService.STATE_TRANSFER_TX_CONFIRMED:
                    {
                        break;
                    }
                    default:
                    {
                        isFinished = true;
                        break;
                    }
                }
            }
            if(isFinished)
            {
                TransferManagerQueueVO forUpdate = new TransferManagerQueueVO();
                forUpdate.setTqIdx(item.getTqIdx());
                forUpdate.setTqTargetState(TransferManagerQueueService.TARGET_STATE_READY);
                forUpdate.setTqFinishDate(new Date());
                update(forUpdate);
            }
        }
    }

    @Override
    public boolean notifyTargetDone(Long targetIdx)
    {
        try
        {
            Map<String, Object> parameter = new HashMap<>();
            parameter.put("tqTargetIdx", targetIdx);
            List<TransferManagerQueueVO> list = selectBySearch(parameter);
            for(TransferManagerQueueVO item : list)
            {
                TransferManagerQueueVO forUpdate = new TransferManagerQueueVO();
                forUpdate.setTqIdx(item.getTqIdx());
                forUpdate.setTqTargetState(TransferManagerQueueService.TARGET_STATE_READY);
                forUpdate.setTqFinishDate(new Date());
                update(forUpdate);
            }
            return list.size() > 0;
        }
        catch (Exception e)
        {
            logger.error(e.toString());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Integer countDistinctWorker()
    {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("tqState", TransferManagerQueueService.STATE_ENABLE);
        parameter.put("tqTargetState", TransferManagerQueueService.TARGET_STATE_READY);
        return transferManagerQueueDao.countDistinctWorker(parameter);
    }


    @Override
    public List<TransferManagerQueueVO> selectEnabledByToken(Integer tkIdx)
    {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("tqState", TransferManagerQueueService.STATE_ENABLE);
        parameter.put("tqTargetState", TransferManagerQueueService.TARGET_STATE_READY);
        parameter.put("tqTkIdx", tkIdx);
        return selectBySearch(parameter);
    }

    @Override
    public List<TransferManagerQueueVO> selectEnabledBySymbol(String tkSymbol)
    {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("tqState", TransferManagerQueueService.STATE_ENABLE);
        parameter.put("tqTargetState", TransferManagerQueueService.TARGET_STATE_READY);
        parameter.put("tqTkSymbol", tkSymbol);
        return selectBySearch(parameter);
    }

    @Override
    public List<TransferManagerQueueVO> selectForGasFeeOnly()
    {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("tqState", TransferManagerQueueService.STATE_GAS_FEE_ONLY);
        parameter.put("tqTargetState", TransferManagerQueueService.TARGET_STATE_READY);
        return selectBySearch(parameter);
    }

    @Override
    public PagingData selectPagingBySearch(Map<String, Object> parameter)
    {
        String pageNumberStr = (String) parameter.get("pageNo");
        String pageSizeStr = (String) parameter.get("pageSize");
        int pageNumber = StringUtils.isEmpty(pageNumberStr) ? 1 : Integer.valueOf(pageNumberStr);
        int pageSize = StringUtils.isEmpty(pageSizeStr) ? 20 : Integer.valueOf(pageSizeStr);
        int totalRecode = transferManagerQueueDao.countBySearch(parameter);

        PagingData pagingData = PagingManager.getPagingList(pageNumber, Long.valueOf("" + totalRecode), pageSize, 10);
        parameter.put("startRow", pagingData.getStartRow());
        parameter.put("rowCount", pagingData.getPageSize());

        List<TransferManagerQueueVO> dataList = selectBySearch(parameter);
        pagingData.setObject(dataList);
        pagingData.setCurrentPageRowCount(dataList.size());

        return pagingData;
    }

    @Override
    public BaseResponse refreshErc20Point(Integer targetIdx)
    {
        BaseResponse response = new BaseResponse();
        ReturnCode returnCode = ReturnCode.INTERNAL_ERROR;
        String extraMessage = null;
        try
        {
            {
                TransferManagerQueueVO transferManagerQueueVO = select(targetIdx);
                TokenInfoVO tokenInfoVO = tokenInfoService.select(transferManagerQueueVO.getTqTkIdx());
                String withdrawAccount = transferManagerQueueVO.getTqAddress();
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
                                        BigDecimal amount = customErc20Util.convertHexToDecimalWithTokenInfo(jsonRpcBalanceOf.getResult(), tokenInfoVO.getTkDecimal());
                                        logger.info(jsonRpcBalanceOf.getResult() + "  " + amount);

                                        TransferManagerQueueVO forUpdate = new TransferManagerQueueVO();
                                        forUpdate.setTqIdx(targetIdx);
                                        forUpdate.setTqErc20Point(amount);
                                        forUpdate.setTqErc20Date(now);
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

                                        List<TransferManagerQueueVO> list = selectAll();
                                        for(TransferManagerQueueVO item : list)
                                        {
                                            if(withdrawAccount.equalsIgnoreCase(item.getTqAddress()))
                                            {
                                                TransferManagerQueueVO forUpdate = new TransferManagerQueueVO();
                                                forUpdate.setTqIdx(item.getTqIdx());
                                                forUpdate.setTqEthPoint(amount);
                                                forUpdate.setTqErc20Date(now);
                                                update(forUpdate);
                                            }
                                        }
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
    public BaseResponse toggleWorker(Integer targetIdx)
    {
        BaseResponse response = new BaseResponse();
        ReturnCode returnCode = ReturnCode.INTERNAL_ERROR;
        String extraMessage = null;
        try
        {
            TransferManagerQueueVO transferManagerQueueVO = select(targetIdx);
            TransferManagerQueueVO forUpdate = new TransferManagerQueueVO();
            forUpdate.setTqIdx(transferManagerQueueVO.getTqIdx());
            if(transferManagerQueueVO.getTqState() == 0)
                forUpdate.setTqState(1);
            else
                forUpdate.setTqState(0);
            if(update(forUpdate))
                returnCode = ReturnCode.SUCCESS;
        }
        catch (Exception e)
        {
            logger.error(e.toString());
            extraMessage = e.toString();
        }
        response.setReturnCode(returnCode);
        response.setDescription(response.getReturnCode().getMessage());
        response.setExtraMessage(extraMessage);
        return response;
    }
}