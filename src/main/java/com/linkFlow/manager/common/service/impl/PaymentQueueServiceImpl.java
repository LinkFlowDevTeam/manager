package com.linkFlow.manager.common.service.impl;

import com.linkFlow.manager.common.config.CommonConstants;
import com.linkFlow.manager.common.config.ConfigDataManager;
import com.linkFlow.manager.common.dao.PaymentQueueDao;
import com.linkFlow.manager.common.model.BaseResponse;
import com.linkFlow.manager.common.model.ReturnCode;
import com.linkFlow.manager.common.model.vo.*;
import com.linkFlow.manager.common.service.*;
import com.linkFlow.manager.common.util.CustomComponentUtil;
import com.linkFlow.manager.common.util.CustomErc20Util;
import com.test32.common.model.blockChain.erc20.Erc20TxResponse;
import com.test32.common.model.blockChain.erc20.jsonRpc.JsonRpcGetTransactionByHash;
import com.test32.common.model.blockChain.erc20.jsonRpc.JsonRpcGetTransactionReceipt;
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
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentQueueServiceImpl implements PaymentQueueService
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired private CustomComponentUtil customComponentUtil;
    @Autowired private CustomErc20Util customErc20Util;
    @Autowired private ConfigDataManager configDataManager;
    @Autowired private PaymentQueueDao paymentQueueDao;
    @Autowired private MemberService memberService;
    @Autowired private TokenInfoService tokenInfoService;
    @Autowired private TokenPointService tokenPointService;
    @Autowired private TransactionService transactionService;
    @Autowired private TransferManagerQueueService transferManagerQueueService;
    @Autowired private ProductInfoService productInfoService;
    @Autowired private ProductUserService productUserService;
    @Autowired private MemberTierService memberTierService;
    @Autowired private SysDataService sysDataService;
    @Autowired private LfUserService lfUserService;

    @Override public boolean insert(PaymentQueueVO entity) { logger.error("not implemented"); return false; }
    @Override public boolean insertNotExist(PaymentQueueVO entity) { return paymentQueueDao.insertNotExist(entity); }
    @Override public boolean copyToCanceled(PaymentQueueVO entity) { return paymentQueueDao.copyToCanceled(entity); }
    @Override public Long countBySearch(Map<String, Object> parameter) { return paymentQueueDao.countBySearch(parameter); }
    @Override public PaymentQueueVO select(Long index) { return paymentQueueDao.selectByIndex(index); }
    @Override public List<PaymentQueueVO> selectAll() { return paymentQueueDao.selectAll(); }
    @Override public List<PaymentQueueVO> selectBySearch(Map<String, Object> parameter) { return paymentQueueDao.selectBySearch(parameter); }
    @Override public boolean update(PaymentQueueVO entity) { return paymentQueueDao.update(entity); }
    @Override public boolean delete(Long index) { return paymentQueueDao.deleteByIndex(index); }

    @Override public boolean increaseErrorCount(Long pqIdx) { Map<String, Object> parameter = new HashMap<>(); parameter.put("pqIdx", pqIdx); return paymentQueueDao.increaseErrorCount(parameter); }
    @Override public boolean updateWithPriorState(Map<String, Object> parameter) { return paymentQueueDao.updateWithPriorState(parameter); }

    @Override
    public PagingData selectPagingBySearch(Map<String, Object> parameter)
    {
        String pageNumberStr = (String) parameter.get("pageNo");
        String pageSizeStr = (String) parameter.get("pageSize");
        int pageNumber = StringUtils.isEmpty(pageNumberStr) ? 1 : Integer.valueOf(pageNumberStr);
        int pageSize = StringUtils.isEmpty(pageSizeStr) ? 20 : Integer.valueOf(pageSizeStr);
        Long totalRecode = paymentQueueDao.countBySearch(parameter);

        PagingData pagingData = PagingManager.getPagingList(pageNumber, totalRecode, pageSize, 10);
        parameter.put("startRow", pagingData.getStartRow());
        parameter.put("rowCount", pagingData.getPageSize());

        List<PaymentQueueVO> dataList = selectBySearch(parameter);
        pagingData.setObject(dataList);
        pagingData.setCurrentPageRowCount(dataList.size());

        return pagingData;
    }

    @Override
    public PaymentQueueVO selectLastByType(int type)
    {
        Map<String, Object> map = new HashMap<>();
        map.put("pqPaymentType", type);
        map.put("orderColumn", "pq_idx");
        map.put("order", "DESC");
        map.put("limit", 1);
        List<PaymentQueueVO> list = selectBySearch(map);
        if(list.size() > 0)
            return list.get(0);
        return null;
    }

    @Override
    public PaymentQueueVO selectByTypeUnique(int type, String uniqueId)
    {
        Map<String, Object> map = new HashMap<>();
        map.put("pqPaymentType", type);
        map.put("pqUniqueId", uniqueId);
        List<PaymentQueueVO> list = selectBySearch(map);
        if(list.size() == 1)
            return list.get(0);
        return null;
    }

    private void markAsTransferRequested(PaymentQueueVO item, BigInteger gasLimit, BigInteger gasPrice, Long nonce, String fromAddress)
    {
        PaymentQueueVO forUpdate = new PaymentQueueVO();
        forUpdate.setPqIdx(item.getPqIdx());
        forUpdate.setPqState(PaymentQueueService.STATE_TRANSFER_REQUESTED);
        if(gasLimit != null)
            forUpdate.setPqGasLimit(gasLimit.intValue());
        if(gasPrice != null)
            forUpdate.setPqGasPrice(gasPrice.longValue());
        forUpdate.setPqRqDate(customComponentUtil.getDatabaseNow());
        if(nonce != null)
            forUpdate.setPqNonce(nonce);
        if( ! StringUtils.isEmpty(fromAddress))
            forUpdate.setPqSendFrom(fromAddress);
        update(forUpdate);
    }

    private void markAsDepositApplied(PaymentQueueVO item, TokenPointVO tokenPointVO)
    {
        PaymentQueueVO forUpdate = new PaymentQueueVO();
        forUpdate.setPqIdx(item.getPqIdx());
        forUpdate.setPqState(PaymentQueueService.STATE_PROCESSED);
        forUpdate.setPqBefore(tokenPointVO.getTpPoint());
        forUpdate.setPqAfter(forUpdate.getPqBefore().add(item.getPqAmount()));
        update(forUpdate);
    }

    private void markAsTransferSuccess(PaymentQueueVO item, String txId)
    {
        PaymentQueueVO forUpdate = new PaymentQueueVO();
        forUpdate.setPqIdx(item.getPqIdx());
        forUpdate.setPqState(PaymentQueueService.STATE_TRANSFER_SUCCESS);
        forUpdate.setPqResultData(txId);
        update(forUpdate);
    }

    private void markAsTransferError(PaymentQueueVO item, String txId)
    {
        PaymentQueueVO forUpdate = new PaymentQueueVO();
        forUpdate.setPqIdx(item.getPqIdx());
        forUpdate.setPqState(PaymentQueueService.STATE_TRANSFER_ERROR);
        forUpdate.setPqResultData(txId);
        update(forUpdate);
    }

    private void markAsTransferPending(PaymentQueueVO item, String txId)
    {
        PaymentQueueVO forUpdate = new PaymentQueueVO();
        forUpdate.setPqIdx(item.getPqIdx());
        forUpdate.setPqState(PaymentQueueService.STATE_TRANSFER_TX_PENDING);
        // maybe useless
        forUpdate.setPqResultData(txId);
        forUpdate.setPqTxHash(txId);
        update(forUpdate);
    }

    private void markAsError(PaymentQueueVO item, String errorMessage)
    {
        PaymentQueueVO forUpdate = new PaymentQueueVO();
        forUpdate.setPqIdx(item.getPqIdx());
        forUpdate.setPqState(PaymentQueueService.STATE_ERROR);
        forUpdate.setPqResultData(errorMessage);
        update(forUpdate);
    }

    private void markAsWithdrawalDeniedSuccess(PaymentQueueVO item)
    {
        TokenPointVO tokenPointVO = tokenPointService.selectByMemberTokenIdx(item.getPqMbIdx(), item.getPqTkIdx());

        PaymentQueueVO forUpdate = new PaymentQueueVO();
        forUpdate.setPqIdx(item.getPqIdx());
        forUpdate.setPqState(PaymentQueueService.STATE_WITHDRAW_DENIED_SUCCESS);
        forUpdate.setPqBefore(tokenPointVO.getTpPoint());
        forUpdate.setPqAfter(forUpdate.getPqBefore().add(item.getPqAmount()));
        update(forUpdate);
    }

    @Override
    public BaseResponse markAsWithdrawSuccess(PaymentQueueVO entity, boolean isFromManager)
    {
        BaseResponse response = new BaseResponse();
        ReturnCode returnCode = ReturnCode.INTERNAL_ERROR;
        String extraMessage = "";
        try
        {
            if(isFromManager && (entity.getPqState().equals(PaymentQueueService.STATE_NONE) || entity.getPqState().equals(PaymentQueueService.STATE_TRANSFER_ERROR) || entity.getPqState().equals(PaymentQueueService.STATE_TRANSFER_REQUESTED)))
            {
                markAsTransferSuccess(entity, null);
                returnCode = ReturnCode.SUCCESS;
            }
            else
            {
                // for scheduler
                extraMessage = "current state is not right. ";
                logger.error(extraMessage + entity.toString());
                returnCode = ReturnCode.INVALID_PRIOR_STATE;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error(e.toString());
            extraMessage = e.toString();
        }
        response.setReturnCode(returnCode);
        response.setDescription(response.getReturnCode().getMessage());
        response.setExtraMessage(extraMessage);
        return response;
    }

    @Override
    public BaseResponse markAsWithdrawScheduled(PaymentQueueVO entity, boolean isFromManager)
    {
        BaseResponse response = new BaseResponse();
        ReturnCode returnCode = ReturnCode.INTERNAL_ERROR;
        String extraMessage = "";
        try
        {
            if(isFromManager && (entity.getPqState().equals(PaymentQueueService.STATE_NONE) || entity.getPqState().equals(PaymentQueueService.STATE_TRANSFER_ERROR) || entity.getPqState().equals(PaymentQueueService.STATE_TRANSFER_REQUESTED)))
            {
                PaymentQueueVO forUpdate = new PaymentQueueVO();
                forUpdate.setPqIdx(entity.getPqIdx());
                forUpdate.setPqState(PaymentQueueService.STATE_WITHDRAW_SCHEDULED_INSERT);
                update(forUpdate);

                returnCode = ReturnCode.SUCCESS;
            }
            else
            {
                // for scheduler
                extraMessage = "current state is not right. ";
                logger.error(extraMessage + entity.toString());
                returnCode = ReturnCode.INVALID_PRIOR_STATE;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error(e.toString());
            extraMessage = e.toString();
        }
        response.setReturnCode(returnCode);
        response.setDescription(response.getReturnCode().getMessage());
        response.setExtraMessage(extraMessage);
        return response;
    }

    @Override
    public BaseResponse applyWithdraw(PaymentQueueVO entity, boolean isFromManager)
    {
        BigInteger gasLimit;
        if(entity.getPqGasLimit() != null)
            gasLimit = new BigInteger("" + entity.getPqGasLimit());
        else
            gasLimit = customErc20Util.getErc20GasLimit();
        return applyWithdrawWithGasLimit(entity, isFromManager, gasLimit, null, customErc20Util.getErc20GasPricePercent());
    }

    @Override
    public BaseResponse applyWithdrawWithGasLimit(PaymentQueueVO entity, boolean isFromManager, BigInteger gasLimit, BigInteger gasPrice, Integer gasPriceMultiplier)
    {
        BaseResponse response = new BaseResponse();
        ReturnCode returnCode = ReturnCode.INTERNAL_ERROR;
        String extraMessage = "";
        try
        {
            String uniqueId = "" + entity.getPqIdx();

            String symbol = entity.getPqSymbol();
            //entity.setPqMbIdx(memberVO.getMbIdx());
            //MemberVO memberVO = memberService.select(entity.getPqMbIdx());
            ProductInfoVO productInfoVO = productInfoService.select(entity.getPqMbIdx().intValue());
            TokenInfoVO tokenInfoVO = tokenInfoService.selectBySymbol(symbol);
            //if(memberVO == null) { logger.error("member not found"); markAsError(entity, "member not found"); }
            if(productInfoVO == null) { logger.error("productInfoVO not found"); markAsError(entity, "member not found"); }
            else if(tokenInfoVO == null) { logger.error("invalid token.  " + entity.toString()); markAsError(entity, "invalid token"); }
            else if(transactionService.isUniqueIdExist(TransactionService.TYPE_WITHDRAW_SUCCESS, uniqueId)) { logger.error("already processed transaction.  " + entity.toString()); markAsTransferSuccess(entity, null); returnCode = ReturnCode.REQUEST_ALREADY_PROCESSED; }
            else if(transactionService.isUniqueIdExist(TransactionService.TYPE_WITHDRAW_DENIED, uniqueId)) { logger.error("already withdraw success transaction.  " + entity.toString()); returnCode = ReturnCode.REQUEST_ALREADY_PROCESSED; } else if(tokenInfoVO.getTkLockWithdraw() == 1) { logger.info("withdrawal is limited by config. " + entity.toString()); returnCode = ReturnCode.TOKEN_WITHDRAWAL_LOCKED; }
            else if( ! isFromManager && ! entity.getPqState().equals(PaymentQueueService.STATE_NONE)) { extraMessage = "current state is not right. "; logger.info(extraMessage + entity.toString()); returnCode = ReturnCode.INVALID_PRIOR_STATE; }
            else if(isFromManager && ! (entity.getPqState().equals(PaymentQueueService.STATE_NONE) || entity.getPqState().equals(PaymentQueueService.STATE_TRANSFER_ERROR) || entity.getPqState().equals(PaymentQueueService.STATE_TRANSFER_REQUESTED) || entity.getPqState().equals(PaymentQueueService.STATE_WITHDRAW_SCHEDULED_INSERT) ))
            {
                // for scheduler
                extraMessage = "current state is not right. ";
                logger.info(extraMessage + entity.toString());
                returnCode = ReturnCode.INVALID_PRIOR_STATE;
            }
            else
            {
                return processErc20Transfer(entity, tokenInfoVO, productInfoVO, gasLimit, gasPrice, gasPriceMultiplier);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error(e.toString());
            extraMessage = e.toString();
        }
        response.setReturnCode(returnCode);
        response.setDescription(response.getReturnCode().getMessage());
        response.setExtraMessage(extraMessage);
        return response;
    }


    private BaseResponse processErc20Transfer(PaymentQueueVO entity, TokenInfoVO tokenInfoVO, ProductInfoVO productInfoVO, BigInteger gasLimit, BigInteger gasPrice, Integer gasPriceMultiplier)
    {
        BaseResponse response = new BaseResponse();
        ReturnCode returnCode = ReturnCode.INTERNAL_ERROR;
        String extraMessage = null;
        try
        {
            String to = entity.getPqSendTo();
            String memo = entity.getPqSendMemo();
            String quantity = entity.getPqAmount().toPlainString() + " " + entity.getPqSymbol();

            if(gasPriceMultiplier == null)
                gasPriceMultiplier = customErc20Util.getErc20GasPricePercent();

            if(gasPrice == null)
                gasPrice = customErc20Util.getGasPrice().getGasPrice();
            if(gasPriceMultiplier != null && gasPriceMultiplier > 0)
            {
                try
                {
                    // input: 20 %
                    BigDecimal mulPercent = new BigDecimal("" + gasPriceMultiplier).multiply(new BigDecimal("0.01"));
                    if(mulPercent.compareTo( new BigDecimal("0.3")) > 0)
                    {
                        logger.error("over 20%.  ignore percent");
                    }
                    else
                    {
                        BigInteger addAmount = new BigDecimal("" + gasPrice).multiply(mulPercent).toBigInteger();
                        logger.info("gasPrice change: " + gasPrice + " + " + addAmount);
                        gasPrice = gasPrice.add(addAmount);
                    }
                }
                catch (Exception subE)
                {
                    logger.error(subE.toString());
                }
            }

            if(StringUtils.isEmpty(to) || ! to.toLowerCase().startsWith("0x"))
            {
                returnCode = ReturnCode.INVALID_REQUEST_DATA;
                extraMessage = "invalid ERC20 address";
                markAsTransferError(entity, extraMessage);
                customComponentUtil.notifySlackWithdrawError("관리자", to, quantity, memo, extraMessage);
            }
            else
            {
                String fromAddress = null;
                String fromKey = null;

                boolean isValidWithdrawConfig = false;
                TransferManagerQueueVO transferManagerQueueVO_selected = null;

                {
                    // 분할 계정에서 지급
                    List<TransferManagerQueueVO> tqList = transferManagerQueueService.selectEnabledByToken(tokenInfoVO.getTkIdx());
                    if(tqList.size() > 0)
                    {
                        transferManagerQueueVO_selected = tqList.get(0);
                        fromAddress = transferManagerQueueVO_selected.getTqAddress();
                        fromKey = customComponentUtil.getErc20MultipleWithdrawPrivateKey(fromAddress);
                        if( ! StringUtils.isEmpty(fromAddress) && ! StringUtils.isEmpty(fromKey))
                            isValidWithdrawConfig = true;
                    }
                }

                if( ! isValidWithdrawConfig)
                {
                    returnCode = ReturnCode.INVALID_CONFIG;
                    extraMessage = "withdraw key is not valid";
                }
                else
                {
                    BigInteger nonce = customErc20Util.getNonce(fromAddress);
                    // ## set flag first
                    markAsTransferRequested(entity, gasLimit, gasPrice, nonce.longValue(), fromAddress);

                    {
                        TransferManagerQueueVO forUpdate = new TransferManagerQueueVO();
                        forUpdate.setTqIdx(transferManagerQueueVO_selected.getTqIdx());
                        forUpdate.setTqTargetState(TransferManagerQueueService.TARGET_STATE_WORKING);
                        forUpdate.setTqTargetIdx(entity.getPqIdx());
                        transferManagerQueueService.update(forUpdate);
                    }

                    Erc20TxResponse erc20TxResponse;
                    if(entity.getPqSymbol().equalsIgnoreCase("ETH"))
                    {
                        erc20TxResponse = customErc20Util.sendEthByRaw(fromKey, fromAddress, to, nonce, entity.getPqAmount(), gasPriceMultiplier);
                    }
                    else
                    {
                        BigInteger erc20Amount = customErc20Util. convertErc20TransferAmount(tokenInfoVO, entity.getPqAmount());
                        erc20TxResponse = customErc20Util.sendErc20TokenByKey(productInfoVO, Erc20QueueService.IO_WITHDRAW, tokenInfoVO, fromKey, fromAddress, to, entity.getPqAmount(), erc20Amount, gasLimit, gasPrice, nonce);
                    }

                    if(erc20TxResponse.getReturnCode() == ReturnCode.SUCCESS)
                    {
                        String txHash = erc20TxResponse.getTransaction().getTransactionHash();
                        // change to pending
                        markAsTransferPending(entity, txHash);

                        {
                            TransferManagerQueueVO forUpdate = new TransferManagerQueueVO();
                            forUpdate.setTqIdx(transferManagerQueueVO_selected.getTqIdx());
                            forUpdate.setTqTargetKey(txHash);
                            forUpdate.setTqRequestDate(new Date());
                            transferManagerQueueService.update(forUpdate);
                        }
                        returnCode = ReturnCode.SUCCESS;
                    }
                    else
                    {
                        if(erc20TxResponse.getTransaction() != null && erc20TxResponse.getTransaction().getError() != null)
                            extraMessage = erc20TxResponse.getTransaction().getError().getMessage() + " " + erc20TxResponse.getTransaction().getError().getData();
                        else if( ! StringUtils.isEmpty(erc20TxResponse.getExtraMessage()))
                            extraMessage = erc20TxResponse.getExtraMessage();

                        markAsTransferError(entity, extraMessage);
                        logger.error("" + erc20TxResponse);
                        customComponentUtil.notifySlackWithdrawError("관리자", to, quantity, memo, extraMessage);
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.error(e.toString());
            extraMessage = e.toString();
        }
        response.setReturnCode(returnCode);
        response.setDescription(response.getReturnCode().getMessage());
        response.setExtraMessage(extraMessage);
        return response;
    }


    @Override
    public BaseResponse denyWithdraw(PaymentQueueVO entity, boolean isFromManager)
    {
        BaseResponse response = new BaseResponse();
        ReturnCode returnCode = ReturnCode.INTERNAL_ERROR;
        String extraMessage = "";
        try
        {
            TokenInfoVO tokenInfoVO = tokenInfoService.selectBySymbol(entity.getPqSymbol());
            if(tokenInfoVO == null)
            {
                extraMessage = "token data not found";
                logger.error(extraMessage);
                returnCode = ReturnCode.DATA_NOT_FOUND;
            }
            else
            {
                if(entity.getPqState().equals(PaymentQueueService.STATE_NONE) || entity.getPqState().equals(PaymentQueueService.STATE_TRANSFER_REQUESTED) || entity.getPqState().equals(PaymentQueueService.STATE_TRANSFER_ERROR))
                {
                    // accept only none
                    Map<String, Object> requestMap = new HashMap<>();
                    requestMap.put("pqIdx", entity.getPqIdx());
                    requestMap.put("pqPriorState", entity.getPqState());
                    requestMap.put("pqState", PaymentQueueService.STATE_WITHDRAW_DENIED_REQUEST);
                    if(paymentQueueDao.updateWithPriorState(requestMap))
                        returnCode = ReturnCode.SUCCESS;
                }
                else
                {
                    extraMessage = "invalid state.  state: " + entity.getPqState();
                    logger.error(extraMessage);
                    returnCode = ReturnCode.INVALID_PRIOR_STATE;
                }
            }
        }
        catch (Exception e)
        {
            logger.error(e.toString());
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        response.setReturnCode(returnCode);
        response.setDescription(response.getReturnCode().getMessage());
        response.setExtraMessage(extraMessage);
        return response;
    }

    @Override
    public BaseResponse denyWithdrawWithMessage(PaymentQueueVO entity, String message)
    {
        BaseResponse response = new BaseResponse();
        ReturnCode returnCode = ReturnCode.INTERNAL_ERROR;
        String extraMessage = "";
        try
        {
            TokenInfoVO tokenInfoVO = tokenInfoService.selectBySymbol(entity.getPqSymbol());
            if(tokenInfoVO == null)
            {
                extraMessage = "token data not found";
                logger.error(extraMessage);
                returnCode = ReturnCode.DATA_NOT_FOUND;
            }
            else
            {
                if(entity.getPqState().equals(PaymentQueueService.STATE_NONE) || entity.getPqState().equals(PaymentQueueService.STATE_TRANSFER_REQUESTED) || entity.getPqState().equals(PaymentQueueService.STATE_TRANSFER_ERROR))
                {
                    // accept only none
                    Map<String, Object> requestMap = new HashMap<>();
                    requestMap.put("pqIdx", entity.getPqIdx());
                    requestMap.put("pqPriorState", entity.getPqState());
                    requestMap.put("pqState", PaymentQueueService.STATE_WITHDRAW_DENIED_REQUEST);
                    requestMap.put("pqResultData", message);
                    if(paymentQueueDao.updateWithPriorState(requestMap))
                        returnCode = ReturnCode.SUCCESS;
                }
                else
                {
                    extraMessage = "invalid state.  state: " + entity.getPqState();
                    logger.error(extraMessage);
                    returnCode = ReturnCode.INVALID_PRIOR_STATE;
                }
            }
        }
        catch (Exception e)
        {
            logger.error(e.toString());
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        response.setReturnCode(returnCode);
        response.setDescription(response.getReturnCode().getMessage());
        response.setExtraMessage(extraMessage);
        return response;
    }

    @Override
    @Transactional("apiTransactionManager")
    public void denyWithdraw(PaymentQueueVO entity)
    {
        try
        {
            String queryTransactionId = "" + entity.getPqIdx();

            MemberVO memberVO = memberService.select(entity.getPqMbIdx());
            TokenInfoVO tokenInfoVO = tokenInfoService.selectBySymbol(entity.getPqSymbol());
            if(memberVO == null)
            {
                logger.error("member not found");
                markAsError(entity, "member not found");
            }
            else if(tokenInfoVO == null)
            {
                logger.error("invalid token.  " + entity.toString());
                markAsError(entity, "invalid token");
            }
            else if(transactionService.isUniqueIdExist(TransactionService.TYPE_WITHDRAW_SUCCESS, queryTransactionId))
            {
                logger.error("already withdraw success transaction.  " + entity.toString());
                //markAsError(entity, null);
            }
            else if(transactionService.isUniqueIdExist(TransactionService.TYPE_WITHDRAW_DENIED, queryTransactionId))
            {
                logger.error("already processed transaction.  " + entity.toString());
                markAsWithdrawalDeniedSuccess(entity);
            }
            else
            {
                TokenPointVO tokenPointVO = tokenPointService.selectByMemberTokenIdx(memberVO.getMbIdx(), tokenInfoVO.getTkIdx());

                boolean isPointSuccess = tokenPointService.addPoint(tokenPointVO, entity.getPqReqAmount());
                boolean isTxSuccess = transactionService.addWithdrawDenied(memberVO, tokenPointVO, entity.getPqReqAmount(), queryTransactionId);
                if(isPointSuccess && isTxSuccess)
                {
                    String to = entity.getPqEtTo();
                    String memo = entity.getPqEtFrom();
                    customComponentUtil.notifySlackWithdrawRequest(memberVO, to, entity.getPqReqAmount(), entity.getPqSymbol(), memo, "출금거부");

                    markAsWithdrawalDeniedSuccess(entity);
                }
                else
                {
                    // try next time
                    logger.error("isPointSuccess: " + isPointSuccess);
                    logger.error("isTxSuccess: " + isTxSuccess);
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
            }
        }
        catch (Exception e)
        {
            logger.error(e.toString());
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }

    @Override
    public void notifyTxChecked(String txHash, JsonRpcGetTransactionByHash jsonRpcGetTransactionByHash)
    {
        try
        {
            Map<String, Object> searchMap = new HashMap<>();
            //searchMap.put("pqPaymentType", CommonConstants.TRANSFER_TYPE_DEPOSIT_BY_EXCEL);
            //searchMap.put("pqState", PaymentQueueService.STATE_TRANSFER_TX_PENDING);
            searchMap.put("pqTxHash", txHash);
            searchMap.put("orderColumn", "pq_idx");
            searchMap.put("order", "DESC");
            List<PaymentQueueVO> list = selectBySearch(searchMap);
            if(list.size() > 1)
            {
                logger.warn("notifyTxChecked: list size: " + list.size());
            }
            if(list.size() > 0)
            {
                PaymentQueueVO forUpdate = new PaymentQueueVO();
                forUpdate.setPqIdx(list.get(0).getPqIdx());
                forUpdate.setPqTxDate(customComponentUtil.getDatabaseNow());
                if(jsonRpcGetTransactionByHash.getResult() == null)
                {
                    forUpdate.setPqTxState(Erc20TxService.TX_STATE_MISSING);
                }
                else if( ! StringUtils.isEmpty(jsonRpcGetTransactionByHash.getResult().getBlockNumber()))
                {
                    forUpdate.setPqTxState(Erc20TxService.TX_STATE_CONFIRMED);
                    forUpdate.setPqBlockNumber(customErc20Util.convertHexToDecimal(jsonRpcGetTransactionByHash.getResult().getBlockNumber()).longValue());
                }
                else
                {
                    forUpdate.setPqTxState(Erc20TxService.TX_STATE_PENDING);
                }
                update(forUpdate);
            }
        }
        catch (Exception e)
        {
            logger.error(e.toString());
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }

    @Override
    @Transactional("apiTransactionManager")
    public boolean insertWithdrawalRequest(MemberVO memberVO, TokenPointVO tokenPointVO, BigDecimal reqAmount, String uniqueId, String transferTo, String transferMemo)
    {
        try
        {
            // 일단 요청금액 절삭
            reqAmount = reqAmount.setScale(CommonConstants.REWARD_CALC_FLOATING_POINT, CommonConstants.INTEREST_CALC_ROUNDING_MODE);

            PaymentQueueVO entity = new PaymentQueueVO();
            entity.setPqPaymentType(PaymentQueueService.TRANSFER_TYPE_WITHDRAW);
            entity.setPqUniqueId(uniqueId);
            entity.setPqTrFlag(PaymentQueueService.TR_FLAG_REAL_TRANSFER);

            entity.setPqMbIdx(memberVO.getMbIdx());


            // sendFrom set by scheduler
            entity.setPqSendTo(transferTo);
            entity.setPqSendMemo(transferMemo);

            entity.setPqEtTo(transferTo);


            entity.setPqState(PaymentQueueService.STATE_NONE);
            entity.setPqTkIdx(tokenPointVO.getTpTkIdx());
            entity.setPqSymbol(tokenPointVO.getTpTkSymbol());


            BigDecimal withdrawalFeeRate;
            BigDecimal finalReturnAmount;


            // calculated return
            TokenInfoVO tokenInfoVO = tokenInfoService.select(tokenPointVO.getTpTkIdx());
            if(StringUtils.isEmpty(tokenInfoVO.getTkWithdrawFee()))
                withdrawalFeeRate = BigDecimal.ZERO;
            else
            {
                // 0.1
                withdrawalFeeRate = new BigDecimal(tokenInfoVO.getTkWithdrawFee());
            }

            // 100.1
            BigDecimal rate = new BigDecimal("100").add(withdrawalFeeRate);

            BigDecimal transferAmount = reqAmount.divide(rate, CommonConstants.REWARD_CALC_FLOATING_POINT, CommonConstants.INTEREST_CALC_ROUNDING_MODE).multiply(new BigDecimal("100"));
            BigDecimal feeAmount = reqAmount.subtract(transferAmount);
            if(feeAmount.compareTo(BigDecimal.ZERO) < 0)
            {
                // 수수료 절삭하다가 마이너스되면
                feeAmount = BigDecimal.ZERO;
            }
            finalReturnAmount = reqAmount.subtract(feeAmount).setScale(tokenInfoVO.getTkRoundingPoint(), CommonConstants.INTEREST_CALC_ROUNDING_MODE);

            entity.setPqFee(feeAmount);

            entity.setPqAmount(finalReturnAmount);
            entity.setPqReqAmount(reqAmount);
            entity.setPqBefore(tokenPointVO.getTpPoint());
            entity.setPqAfter(entity.getPqBefore().subtract(reqAmount));

            if(insertNotExist(entity))
            {
                String queryTransactionId = "" + entity.getPqIdx();
                if(tokenPointService.subtractPoint(tokenPointVO, reqAmount))
                {
                    if(transactionService.addWithdrawRequest(memberVO, tokenPointVO, reqAmount, queryTransactionId, transferTo, withdrawalFeeRate, finalReturnAmount))
                        return true;
                    logger.info("failed to: transactionService.addWithdrawRequest");
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
                else
                {
                    logger.info("out of balance.  mark as error" + entity.toString());
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
            }
        }
        catch (Exception e)
        {
            logger.error(e.toString());
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return false;
    }

    @Override
    @Transactional("apiTransactionManager")
    public void applyProductPurchase(PaymentQueueVO item)
    {
        try
        {
            String queryTransactionId = "" + item.getPqIdx();

            ProductInfoVO productInfoVO = productInfoService.selectByErc20Address(item.getPqEtActualTo());
            if(productInfoVO == null)
            {
                logger.warn("productInfoVO not found: " + item.toString());
            }
            else
            {
                if(transactionService.isUniqueIdExist(TransactionService.TYPE_PRODUCT_PURCHASE_SUCCESS, queryTransactionId))
                {
                    logger.warn("already processed transaction.  " + item.toString());
                    markAsError(item, "already processed transaction");
                }
                else
                {
                    memberService.refreshTierAll();

                    TokenInfoVO tokenInfoVO = tokenInfoService.select(item.getPqTkIdx());
                    MemberVO memberVO = memberService.select(item.getPqMbIdx());
                    MemberTierVO memberTierVO = memberTierService.selectByTier(memberVO.getMbTier());


                    BigDecimal validDepositAmount;
                    BigDecimal refundAmount;

                    if(memberTierVO == null)
                    {
                        // 0 tier 가입불가
                        validDepositAmount = BigDecimal.ZERO;
                        refundAmount = item.getPqReqAmount();
                    }
                    else
                    {
                        if(productInfoVO.getPdState() == ProductInfoService.STATE_ON_SALE || productInfoVO.getPdState() == ProductInfoService.STATE_STARTED)
                        {
                            BigDecimal currentRequestAmount = item.getPqAmount();
                            ProductUserVOForSum productUserVOForSum = productUserService.selectSumDepositAmount(item.getPqMbIdx(), productInfoVO.getPdIdx());
                            BigDecimal sumDepositAmount = productUserVOForSum.getAmount();

                            BigDecimal maxDepositLimit = new BigDecimal("" + memberTierVO.getMtLimitAmount());
                            BigDecimal remainLimitAmount = maxDepositLimit.subtract(sumDepositAmount);
                            if(remainLimitAmount.compareTo(BigDecimal.ZERO) < 0)
                                remainLimitAmount = BigDecimal.ZERO;
                            validDepositAmount = currentRequestAmount;
                            refundAmount = BigDecimal.ZERO;
                            if(remainLimitAmount.compareTo(validDepositAmount) < 0)
                            {
                                validDepositAmount = remainLimitAmount;
                                refundAmount = currentRequestAmount.subtract(remainLimitAmount);
                            }
                        }
                        else
                        {
                            validDepositAmount = BigDecimal.ZERO;
                            refundAmount = item.getPqReqAmount();
                        }
                    }

                    if(validDepositAmount.compareTo(BigDecimal.ZERO) < 0 || refundAmount.compareTo(BigDecimal.ZERO) < 0)
                    {
                        logger.error("invalid calculation: " + item);
                        logger.error("validDepositAmount: " + validDepositAmount.toPlainString());
                        logger.error("refundAmount: " + refundAmount.toPlainString());
                        return;
                    }

                    {
                        boolean isPuSuccess;
                        boolean isRefundSuccess;
                        boolean isPdSuccess;
                        boolean isPqSuccess;

                        boolean isTxSuccess;
                        boolean isTxRemain;

                        if(validDepositAmount.compareTo(BigDecimal.ZERO) <= 0)
                            isPuSuccess = true;
                        else
                        {
                            ProductUserVO entity = productUserService.selectByMemberProduct(item.getPqMbIdx(), productInfoVO.getPdIdx());
                            if (entity == null)
                            {
                                entity = new ProductUserVO();
                                entity.setPuMbIdx(item.getPqMbIdx());
                                entity.setPuPdIdx(productInfoVO.getPdIdx());
                                entity.setPuTkIdx(tokenInfoVO.getTkIdx());
                                entity.setPuBaseAmount(validDepositAmount);

                                entity.setPuJoinDay(productInfoVO.getPdDay());
                                entity.setPuRewardRate(productInfoVO.getPdInterestRate());

                                isPuSuccess = productUserService.insert(entity);
                            }
                            else
                            {
                                isPuSuccess = productUserService.appendBaseAmount(entity, validDepositAmount);
                            }
                        }

                        if(refundAmount.compareTo(BigDecimal.ZERO) <= 0)
                            isRefundSuccess = true;
                        else
                        {
                            TokenPointVO tokenPointVO = tokenPointService.selectByMemberTokenIdxOrCreate(memberVO.getMbIdx(), tokenInfoVO.getTkIdx(), true);
                            isRefundSuccess = tokenPointService.addPoint(tokenPointVO, refundAmount);
                        }

                        isPdSuccess = productInfoService.refreshBaseAmount(productInfoVO.getPdIdx());

                        PaymentQueueVO forUpdate = new PaymentQueueVO();
                        forUpdate.setPqIdx(item.getPqIdx());
                        forUpdate.setPqState(PaymentQueueService.STATE_PROCESSED);
                        isPqSuccess = update(forUpdate);

                        if(validDepositAmount.compareTo(BigDecimal.ZERO) > 0)
                            isTxSuccess = transactionService.addProductPurchaseSuccess(item, memberVO, productInfoVO, validDepositAmount);
                        else
                            isTxSuccess = true;
                        if(refundAmount.compareTo(BigDecimal.ZERO) > 0)
                            isTxRemain = transactionService.addProductPurchaseRemain(item, memberVO, productInfoVO, refundAmount);
                        else
                            isTxRemain = true;

                        if(isPuSuccess && isPdSuccess && isPqSuccess && isRefundSuccess && isTxRemain && isTxSuccess)
                        {
                            logger.info("processed: " + item);
                        }
                        else
                        {
                            logger.error("failed to process: " + item);
                            logger.error("isPuSuccess: " + isPuSuccess);
                            logger.error("isPdSuccess: " + isPdSuccess);
                            logger.error("isRefundSuccess: " + isRefundSuccess);
                            logger.error("isPqSuccess: " + isPqSuccess);
                            logger.error("isTxSuccess: " + isTxSuccess);
                            logger.error("isTxRemain: " + isTxRemain);
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        }
                    }

                }
            }
        }
        catch (Exception e)
        {
            logger.error("" + item);
            logger.error(e.toString());
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }


    @Override
    @Transactional("apiTransactionManager")
    public void applyLfTokenDeposit(PaymentQueueVO item)
    {
        try
        {
            String queryTransactionId = "" + item.getPqIdx();
            if(transactionService.isUniqueIdExist(TransactionService.TYPE_LF_DEPOSIT, queryTransactionId))
            {
                logger.warn("already processed transaction.  " + item.toString());
                markAsError(item, "already processed transaction");
            }
            else
            {
                SysDataVO sysDataVO = sysDataService.selectDefault();
                TokenInfoVO tokenInfoVO = tokenInfoService.selectLfToken();

                LfUserVO entity = new LfUserVO();
                entity.setLuTkIdx(tokenInfoVO.getTkIdx());
                entity.setLuMbIdx(item.getPqMbIdx());
                entity.setLuBaseAmount(item.getPqAmount());
                entity.setLuState(LfUserService.STATE_ACTIVE);

                entity.setLuCreateDate(customComponentUtil.getDatabaseNow());
                entity.setLuExpireDate(CommonDateUtil.addDay(entity.getLuCreateDate(), sysDataVO.getSsLfTokenLockDay()));
                boolean isPuSuccess = lfUserService.insert(entity);


                PaymentQueueVO forUpdate = new PaymentQueueVO();
                forUpdate.setPqIdx(item.getPqIdx());
                forUpdate.setPqState(PaymentQueueService.STATE_PROCESSED);
                boolean isPqSuccess = update(forUpdate);

                boolean isTxSuccess = transactionService.addLfDeposit(item, tokenInfoVO);

                if(isPuSuccess && isPqSuccess && isTxSuccess)
                {
                    logger.info("processed: " + item);

                    memberService.refreshTierAll();
                    memberService.refreshLfTokenLocked(item.getPqMbIdx());
                }
                else
                {
                    logger.error("failed to process: " + item);
                    logger.error("isPuSuccess: " + isPuSuccess);
                    logger.error("isPqSuccess: " + isPqSuccess);
                    logger.error("isTxSuccess: " + isTxSuccess);
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                }
            }
        }
        catch (Exception e)
        {
            logger.error("" + item);
            logger.error(e.toString());
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }

    @Override
    @Transactional("apiTransactionManager")
    public void notifyReceiptChecked(Erc20TxVO erc20TxVO, JsonRpcGetTransactionReceipt jsonRpcGetTransactionReceipt, boolean isForceRetry)
    {
        try
        {
            Map<String, Object> searchMap = new HashMap<>();
            //searchMap.put("pqPaymentType", CommonConstants.TRANSFER_TYPE_DEPOSIT_BY_EXCEL);
            //searchMap.put("pqState", PaymentQueueService.STATE_TRANSFER_TX_PENDING);
            searchMap.put("pqTxHash", erc20TxVO.getEtTxHash());
            searchMap.put("orderColumn", "pq_idx");
            searchMap.put("order", "DESC");
            List<PaymentQueueVO> list = selectBySearch(searchMap);
            if(list.size() > 1)
            {
                logger.warn("notifyTxChecked: list size: " + list.size());
            }
            if(list.size() > 0)
            {
                PaymentQueueVO entity = list.get(0);
                String to = entity.getPqEtTo();
                String memo = entity.getPqSendMemo();
                String quantity = entity.getPqAmount() + " " + entity.getPqSymbol();

                PaymentQueueVO forUpdate = new PaymentQueueVO();
                forUpdate.setPqIdx(list.get(0).getPqIdx());
                forUpdate.setPqRsDate(customComponentUtil.getDatabaseNow());

                if(Erc20TxService.STATUS_RECEIPT_SUCCESS.equalsIgnoreCase(jsonRpcGetTransactionReceipt.getResult().getStatus()))
                {
                    forUpdate.setPqRsState(Erc20TxService.RS_STATE_CONFIRMED);
                    forUpdate.setPqState(PaymentQueueService.STATE_TRANSFER_RS_RECEIVED_CONFIRMED);

                    String uniqueId = "" + entity.getPqIdx();
                    String symbol = entity.getPqSymbol();
                    MemberVO memberVO = memberService.select(entity.getPqMbIdx());
                    TokenInfoVO tokenInfoVO = tokenInfoService.selectBySymbol(symbol);
                    TokenPointVO tokenPointVO = tokenPointService.selectByMemberTokenIdx(memberVO.getMbIdx(), tokenInfoVO.getTkIdx());
                    boolean isPqSuccess = update(forUpdate);
                    boolean isTxSuccess = transactionService.addWithdrawSuccess(memberVO, tokenPointVO, entity.getPqAmount(), to, memo, uniqueId, entity.getPqTxHash());
                    if(isPqSuccess && isTxSuccess)
                    {
                        logger.info("withdraw finally success: " + entity.getPqTxHash());
                    }
                    else
                    {
                        logger.info("withdraw finally failed. " + list.get(0));
                        logger.error("isPqSuccess: " + isPqSuccess);
                        logger.error("isTxSuccess: " + isTxSuccess);
                        if( ! isForceRetry)
                        {
                            customComponentUtil.notifySlackWithdrawError("관리자", to, quantity, memo, "DB 업데이트 오류, 체크요망.  idx: " + entity.getPqIdx());
                            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        }
                    }
                }
                else if(Erc20TxService.STATUS_RECEIPT_FAILURE.equalsIgnoreCase(jsonRpcGetTransactionReceipt.getResult().getStatus()))
                {
                    logger.warn("withdraw finally rejected");
                    forUpdate.setPqRsState(Erc20TxService.RS_STATE_REJECTED);
                    forUpdate.setPqState(PaymentQueueService.STATE_TRANSFER_RS_RECEIVED_REJECTED);
                    update(forUpdate);

                    if( ! isForceRetry)
                    {
                        increaseErrorCount(entity.getPqIdx());
                        customComponentUtil.notifySlackWithdrawError("관리자", to, quantity, memo, "트랜잭션 오류, 이더스캔 체크요망");
                    }
                }
                else
                {
                    logger.error("out of case.  status: " + jsonRpcGetTransactionReceipt.getResult().getStatus());
                    logger.error("" + jsonRpcGetTransactionReceipt.getResult().getStatus());
                }

                // release worker item
                transferManagerQueueService.notifyTargetDone(entity.getPqIdx());
            }
        }
        catch (Exception e)
        {
            logger.error(e.toString());
            e.printStackTrace();
            logger.info("" + erc20TxVO);
            logger.info("" + jsonRpcGetTransactionReceipt);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }

    @Override
    public BaseResponse resetTransferErrorState(PaymentQueueVO entity)
    {
        BaseResponse response = new BaseResponse();
        ReturnCode returnCode = ReturnCode.INTERNAL_ERROR;
        String extraMessage = "";
        try
        {
            boolean isValidRequest = false;
            switch (entity.getPqState())
            {
                case PaymentQueueService.STATE_TRANSFER_ERROR:
                case PaymentQueueService.STATE_TRANSFER_TX_MISSING:
                case PaymentQueueService.STATE_TRANSFER_RS_RECEIVED_REJECTED:
                {
                    isValidRequest = true;
                    break;
                }
                default:
                {
                    returnCode = ReturnCode.INVALID_REQUEST_DATA;
                    break;
                }
            }

            if(isValidRequest)
            {
                if(copyToCanceled(entity))
                {
                    Map<String, Object> requestMap = new HashMap<>();
                    requestMap.put("pqIdx", entity.getPqIdx());
                    requestMap.put("pqPriorState", entity.getPqState());
                    if(paymentQueueDao.resetTransferErrorWithPriorState(requestMap))
                        returnCode = ReturnCode.SUCCESS;
                    else
                    {
                        logger.error("failed to: resetTransferErrorWithPriorState");
                        logger.error("" + entity);
                    }
                }
                else
                {
                    logger.error("failed to: copyToCanceled");
                    logger.error("" + entity);
                }
            }
        }
        catch (Exception e)
        {
            logger.error(e.toString());
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        response.setReturnCode(returnCode);
        response.setDescription(response.getReturnCode().getMessage());
        response.setExtraMessage(extraMessage);
        return response;
    }
}