package com.linkFlow.manager.common.service.impl;

import com.linkFlow.manager.common.dao.TransactionDao;
import com.linkFlow.manager.common.model.vo.*;
import com.linkFlow.manager.common.service.TrExtraService;
import com.linkFlow.manager.common.service.TransactionService;
import com.linkFlow.manager.common.util.CustomComponentUtil;
import com.test32.common.paging.PagingData;
import com.test32.common.paging.PagingManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional("apiTransactionManager")
public class TransactionServiceImpl implements TransactionService
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired private CustomComponentUtil customComponentUtil;
    @Autowired private TransactionDao transactionDao;
    @Autowired private TrExtraService trExtraService;

    @Override public boolean insert(TransactionVO entity) { return transactionDao.insert(entity); }
    @Override public TransactionVO select(Long index) { return transactionDao.selectByIndex(index); }
    @Override public List<TransactionVO> selectAll() { return transactionDao.selectAll(); }
    @Override public List<TransactionVO> selectBySearch(Map<String, Object> parameter) { return transactionDao.selectBySearch(parameter); }
    @Override public boolean update(TransactionVO entity) { return transactionDao.update(entity); }
    @Override public boolean delete(Long index) { return transactionDao.deleteByIndex(index); }

    @Override
    public boolean isUniqueIdExist(Integer type, String uniqueId)
    {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("trType", type);
        parameter.put("trUniqueId", uniqueId);
        Long totalRecode = transactionDao.countBySearch(parameter);
        return totalRecode > 0;
    }

    @Override
    public PagingData selectPagingBySearch(Map<String, Object> parameter)
    {
        String pageNumberStr = (String) parameter.get("pageNo");
        String pageSizeStr = (String) parameter.get("pageSize");
        int pageNumber = StringUtils.isEmpty(pageNumberStr) ? 1 : Integer.valueOf(pageNumberStr);
        int pageSize = StringUtils.isEmpty(pageSizeStr) ? 20 : Integer.valueOf(pageSizeStr);
        Long totalRecode = transactionDao.countBySearch(parameter);

        PagingData pagingData = PagingManager.getPagingList(pageNumber, totalRecode, pageSize, 10);
        parameter.put("startRow", pagingData.getStartRow());
        parameter.put("rowCount", pagingData.getPageSize());

        List<TransactionVO> dataList = selectBySearch(parameter);
        pagingData.setObject(dataList);
        pagingData.setCurrentPageRowCount(dataList.size());

        return pagingData;
    }

    @Override
    public PagingData selectPagingBySearchForApi(Map<String, Object> parameter)
    {
        String pageNumberStr = (String) parameter.get("pageNo");
        String pageSizeStr = (String) parameter.get("pageSize");
        int pageNumber = StringUtils.isEmpty(pageNumberStr) ? 1 : Integer.valueOf(pageNumberStr);
        int pageSize = StringUtils.isEmpty(pageSizeStr) ? 20 : Integer.valueOf(pageSizeStr);
        Long totalRecode = transactionDao.countBySearch(parameter);

        PagingData pagingData = PagingManager.getPagingList(pageNumber, totalRecode, pageSize, 10);
        parameter.put("startRow", pagingData.getStartRow());
        parameter.put("rowCount", pagingData.getPageSize());

        List<TransactionVOForApi> dataList = transactionDao.selectBySearchForApi(parameter);
        pagingData.setObject(dataList);
        pagingData.setCurrentPageRowCount(dataList.size());
        return pagingData;
    }

    @Override
    public TransactionVOForApi selectBySearchForApi(Long idx)
    {
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("trIdx", idx);
        List<TransactionVOForApi> dataList = transactionDao.selectBySearchForApi(parameter);
        if(dataList.size() > 0)
            return dataList.get(0);
        return null;
    }

    private void updateNewEntityTime(TransactionVO entity)
    {
        Date nowDate = customComponentUtil.getDatabaseNow();
        SimpleDateFormat resultFormat = customComponentUtil.newSimpleDateFormatFull();
        String dateString = resultFormat.format(nowDate);

        entity.setTrYear(dateString.substring(0, 4));
        entity.setTrMonth(dateString.substring(0, 7));
        entity.setTrDate(dateString.substring(0, 10));
    }

    @Override
    public boolean addWithdrawRequest(MemberVO memberVO, TokenPointVO tokenPointVO, BigDecimal amount, String uniqueId, String to, BigDecimal feeRate, BigDecimal finalReturn)
    {
        TransactionVO entity = new TransactionVO();
        updateNewEntityTime(entity);
        entity.setTrUpdateType(TransactionService.POINT_UPDATE_OUT);
        entity.setTrType(TransactionService.TYPE_WITHDRAW_REQUEST);
        entity.setTrUniqueId(uniqueId);
        entity.setTrState(TransactionService.STATE_PROCESSED);

        entity.setTrMbIdx(memberVO.getMbIdx());
        entity.setTrMbId(memberVO.getMbId());

        entity.setTrTkIdx(tokenPointVO.getTpTkIdx());
        entity.setTrTkSymbol(tokenPointVO.getTpTkSymbol());

        // subtracted already
        entity.setTrAmount(amount);
        entity.setTrPointBefore(tokenPointVO.getTpPoint());
        entity.setTrPointAfter(tokenPointVO.getTpPoint().subtract(amount));

        if(transactionDao.insert(entity))
        {
            if(trExtraService.insertWithdrawRequest(entity.getTrIdx(), memberVO, tokenPointVO, amount, uniqueId, to, feeRate, finalReturn)) return true;
            else { logger.error("failed: trExtraService.insertWithdrawRequest"); TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); return false; }
        }
        else logger.error("failed: transactionDao.insert");
        return false;
    }

    @Override
    public boolean addWithdrawSuccess(MemberVO memberVO, TokenPointVO tokenPointVO, BigDecimal amount, String to, String memo, String uniqueId, String txId)
    {
        TransactionVO entity = new TransactionVO();
        updateNewEntityTime(entity);
        entity.setTrUpdateType(TransactionService.POINT_UPDATE_NONE);
        entity.setTrType(TransactionService.TYPE_WITHDRAW_SUCCESS);
        entity.setTrUniqueId(uniqueId);
        entity.setTrState(TransactionService.STATE_PROCESSED);

        entity.setTrMbIdx(memberVO.getMbIdx());
        entity.setTrMbId(memberVO.getMbId());

        entity.setTrTkIdx(tokenPointVO.getTpTkIdx());
        entity.setTrTkSymbol(tokenPointVO.getTpTkSymbol());

        // subtracted already
        //entity.setTrAmount(BigDecimal.ZERO);
        entity.setTrAmount(amount);
        entity.setTrPointBefore(tokenPointVO.getTpPoint());
        entity.setTrPointAfter(tokenPointVO.getTpPoint());

        if(transactionDao.insert(entity))
        {
            if(trExtraService.insertWithdrawSuccess(entity.getTrIdx(), memberVO, tokenPointVO, amount, to, memo, uniqueId, txId)) return true;
            else { logger.error("failed: trExtraService.insertWithdrawSuccess"); TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); return false; }
        }
        else logger.error("failed: transactionDao.insert");
        return false;
    }

    @Override
    public boolean addWithdrawDenied(MemberVO memberVO, TokenPointVO tokenPointVO, BigDecimal amount, String uniqueId)
    {
        TransactionVO entity = new TransactionVO();
        updateNewEntityTime(entity);
        entity.setTrUpdateType(TransactionService.POINT_UPDATE_IN);
        entity.setTrType(TransactionService.TYPE_WITHDRAW_DENIED);
        entity.setTrUniqueId(uniqueId);
        entity.setTrState(TransactionService.STATE_PROCESSED);

        entity.setTrMbIdx(memberVO.getMbIdx());
        entity.setTrMbId(memberVO.getMbId());

        entity.setTrTkIdx(tokenPointVO.getTpTkIdx());
        entity.setTrTkSymbol(tokenPointVO.getTpTkSymbol());

        // subtracted already
        entity.setTrAmount(amount);
        entity.setTrPointBefore(tokenPointVO.getTpPoint());
        entity.setTrPointAfter(tokenPointVO.getTpPoint().add(amount));


        return transactionDao.insert(entity);
    }

    @Override
    public boolean addProductPurchaseSuccess(PaymentQueueVO paymentQueueVO, MemberVO memberVO, ProductInfoVO productInfoVO, BigDecimal amount)
    {
        TransactionVO entity = new TransactionVO();
        updateNewEntityTime(entity);
        entity.setTrUpdateType(TransactionService.POINT_UPDATE_NONE);
        entity.setTrType(TransactionService.TYPE_PRODUCT_PURCHASE_SUCCESS);
        entity.setTrUniqueId("" + paymentQueueVO.getPqIdx());
        entity.setTrState(TransactionService.STATE_PROCESSED);

        entity.setTrMbIdx(memberVO.getMbIdx());
        entity.setTrMbId(memberVO.getMbId());

        entity.setTrTkIdx(productInfoVO.getPdTkIdx());
        entity.setTrTkSymbol(productInfoVO.getPdTkSymbol());

        entity.setTrAmount(amount);

        if(transactionDao.insert(entity))
        {
            if(trExtraService.insertProductPurchaseSuccess(entity.getTrIdx(), paymentQueueVO, memberVO, productInfoVO, amount)) return true;
            else { logger.error("failed: trExtraService.insertProductPurchaseSuccess"); TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); return false; }
        }
        else logger.error("failed: transactionDao.insert");
        return false;
    }

    @Override
    public boolean addProductPurchaseRemain(PaymentQueueVO paymentQueueVO, MemberVO memberVO, ProductInfoVO productInfoVO, BigDecimal amount)
    {
        TransactionVO entity = new TransactionVO();
        updateNewEntityTime(entity);
        entity.setTrUpdateType(TransactionService.POINT_UPDATE_IN);
        entity.setTrType(TransactionService.TYPE_PRODUCT_PURCHASE_REMAIN);
        entity.setTrUniqueId("" + paymentQueueVO.getPqIdx());
        entity.setTrState(TransactionService.STATE_PROCESSED);

        entity.setTrMbIdx(memberVO.getMbIdx());
        entity.setTrMbId(memberVO.getMbId());

        entity.setTrTkIdx(productInfoVO.getPdTkIdx());
        entity.setTrTkSymbol(productInfoVO.getPdTkSymbol());

        entity.setTrAmount(amount);

        if(transactionDao.insert(entity))
        {
            if(trExtraService.insertProductPurchaseRemain(entity.getTrIdx(), paymentQueueVO, memberVO, productInfoVO, amount)) return true;
            else { logger.error("failed: trExtraService.insertProductPurchaseRemain"); TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); return false; }
        }
        else logger.error("failed: transactionDao.insert");
        return false;
    }

    @Override
    public boolean addProductCancel(ProductUserVO productUserVO, BigDecimal finalReturn, BigDecimal feeAmount, String feeRate)
    {
        TransactionVO entity = new TransactionVO();
        updateNewEntityTime(entity);
        entity.setTrUpdateType(TransactionService.POINT_UPDATE_IN);
        entity.setTrType(TransactionService.TYPE_PRODUCT_CANCEL);
        entity.setTrUniqueId("" + productUserVO.getPuIdx());
        entity.setTrState(TransactionService.STATE_PROCESSED);

        entity.setTrMbIdx(productUserVO.getMemberVO().getMbIdx());
        entity.setTrMbId(productUserVO.getMemberVO().getMbId());

        entity.setTrTkIdx(productUserVO.getPuTkIdx());
        entity.setTrTkSymbol(productUserVO.getProductInfoVO().getPdTkSymbol());

        entity.setTrAmount(finalReturn);

        if(transactionDao.insert(entity))
        {
            if(trExtraService.insertProductCancel(entity.getTrIdx(), productUserVO, finalReturn, feeAmount, feeRate)) return true;
            else { logger.error("failed: trExtraService.insertProductCancel"); TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); return false; }
        }
        else logger.error("failed: transactionDao.insert");
        return false;
    }

    @Override
    public boolean addDailyInterest(ProductInfoVO productInfoVO, ProductUserVO productUserVO, BigDecimal dailyInterest, int interestIndex)
    {
        String uniqueId = "" + productUserVO.getPuIdx() + "_" + interestIndex;

        TransactionVO entity = new TransactionVO();
        updateNewEntityTime(entity);
        entity.setTrUpdateType(TransactionService.POINT_UPDATE_NONE);
        entity.setTrType(TransactionService.TYPE_INTEREST);
        entity.setTrUniqueId(uniqueId);
        entity.setTrState(TransactionService.STATE_PROCESSED);

        entity.setTrMbIdx(productUserVO.getMemberVO().getMbIdx());
        entity.setTrMbId(productUserVO.getMemberVO().getMbId());

        entity.setTrTkIdx(productUserVO.getPuTkIdx());
        entity.setTrTkSymbol(productUserVO.getProductInfoVO().getPdTkSymbol());

        entity.setTrAmount(dailyInterest);

        if(transactionDao.insert(entity))
        {
            if(trExtraService.insertDailyInterest(entity.getTrIdx(), productInfoVO, productUserVO, dailyInterest, interestIndex)) return true;
            else { logger.error("failed: trExtraService.insertDailyInterest"); TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); return false; }
        }
        else logger.error("failed: transactionDao.insert");
        return false;
    }

    @Override
    public boolean addLfDeposit(PaymentQueueVO paymentQueueVO, TokenInfoVO tokenInfoVO)
    {
        TransactionVO entity = new TransactionVO();
        updateNewEntityTime(entity);
        entity.setTrUpdateType(TransactionService.POINT_UPDATE_NONE);
        entity.setTrType(TransactionService.TYPE_LF_DEPOSIT);
        entity.setTrUniqueId("" + paymentQueueVO.getPqIdx());
        entity.setTrState(TransactionService.STATE_PROCESSED);

        entity.setTrMbIdx(paymentQueueVO.getMemberVO().getMbIdx());
        entity.setTrMbId(paymentQueueVO.getMemberVO().getMbId());

        entity.setTrTkIdx(tokenInfoVO.getTkIdx());
        entity.setTrTkSymbol(tokenInfoVO.getTkSymbol());

        entity.setTrAmount(paymentQueueVO.getPqAmount());

        if(transactionDao.insert(entity))
        {
            if(trExtraService.insertLfDeposit(entity.getTrIdx(), paymentQueueVO, tokenInfoVO)) return true;
            else { logger.error("failed: trExtraService.insertLfDeposit"); TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); return false; }
        }
        else logger.error("failed: transactionDao.insert");
        return false;
    }

    @Override
    public boolean addLfUnlock(LfUserVO lfUserVO)
    {
        TransactionVO entity = new TransactionVO();
        updateNewEntityTime(entity);
        entity.setTrUpdateType(TransactionService.POINT_UPDATE_IN);
        entity.setTrType(TransactionService.TYPE_LF_UNLOCK);
        entity.setTrUniqueId("" + lfUserVO.getLuIdx());
        entity.setTrState(TransactionService.STATE_PROCESSED);

        entity.setTrMbIdx(lfUserVO.getMemberVO().getMbIdx());
        entity.setTrMbId(lfUserVO.getMemberVO().getMbId());

        entity.setTrTkIdx(lfUserVO.getTokenInfoVO().getTkIdx());
        entity.setTrTkSymbol(lfUserVO.getTokenInfoVO().getTkSymbol());

        entity.setTrAmount(lfUserVO.getLuBaseAmount());

        if(transactionDao.insert(entity))
        {
            if(trExtraService.insertLfUnlock(entity.getTrIdx(), lfUserVO)) return true;
            else { logger.error("failed: trExtraService.insertLfUnlock"); TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); return false; }
        }
        else logger.error("failed: transactionDao.insert");
        return false;
    }
}