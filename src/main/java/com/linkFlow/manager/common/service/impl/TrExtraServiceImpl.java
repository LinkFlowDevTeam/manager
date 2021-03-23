package com.linkFlow.manager.common.service.impl;

import com.linkFlow.manager.common.dao.TrExtraDao;
import com.linkFlow.manager.common.model.vo.*;
import com.linkFlow.manager.common.service.TrExtraService;
import com.test32.common.paging.PagingData;
import com.test32.common.paging.PagingManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class TrExtraServiceImpl implements TrExtraService
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired private TrExtraDao trExtraDao;

    @Override public boolean insert(TrExtraVO entity) { return trExtraDao.insert(entity); }
    @Override public TrExtraVO select(Long index) { return trExtraDao.selectByIndex(index); }
    @Override public List<TrExtraVO> selectAll() { return trExtraDao.selectAll(); }
    @Override public List<TrExtraVO> selectBySearch(Map<String, Object> parameter) { return trExtraDao.selectBySearch(parameter); }
    @Override public boolean update(TrExtraVO entity) { return trExtraDao.update(entity); }
    @Override public boolean delete(Long index) { return trExtraDao.deleteByIndex(index); }

    @Override
    public PagingData selectPagingBySearch(Map<String, Object> parameter)
    {
        String pageNumberStr = (String) parameter.get("pageNo");
        String pageSizeStr = (String) parameter.get("pageSize");
        int pageNumber = StringUtils.isEmpty(pageNumberStr) ? 1 : Integer.parseInt(pageNumberStr);
        int pageSize = StringUtils.isEmpty(pageSizeStr) ? 20 : Integer.parseInt(pageSizeStr);
        Long totalRecode = trExtraDao.countBySearch(parameter);

        PagingData pagingData = PagingManager.getPagingList(pageNumber, totalRecode, pageSize, 10);
        parameter.put("startRow", pagingData.getStartRow());
        parameter.put("rowCount", pagingData.getPageSize());

        List<TrExtraVO> dataList = selectBySearch(parameter);
        pagingData.setObject(dataList);
        pagingData.setCurrentPageRowCount(dataList.size());

        return pagingData;
    }

    @Override
    public boolean insertBy(long dataIdx, String data1, String data2, String data3, String data4, String data5, String data6)
    {
        TrExtraVO entity = new TrExtraVO();
        entity.setEtTrIdx(dataIdx);
        entity.setEtData1(data1);
        entity.setEtData1(data1);
        entity.setEtData1(data1);
        entity.setEtData1(data1);
        entity.setEtData1(data1);
        entity.setEtData1(data1);
        return insert(entity);
    }


    @Override
    public boolean insertWithdrawRequest(long dataIdx, MemberVO memberVO, TokenPointVO tokenPointVO, BigDecimal amount, String uniqueId, String to, BigDecimal feeRate, BigDecimal finalReturn)
    {
        return insertBy(dataIdx, to, feeRate.toPlainString(), finalReturn.toPlainString(), null, null, null);
    }

    @Override
    public boolean insertWithdrawSuccess(long dataIdx, MemberVO memberVO, TokenPointVO tokenPointVO, BigDecimal amount, String to, String memo, String uniqueId, String txId)
    {
        return insertBy(dataIdx, to, memo, uniqueId, txId, null, null);
    }

    @Override
    public boolean insertProductPurchaseSuccess(long dataIdx, PaymentQueueVO paymentQueueVO, MemberVO memberVO, ProductInfoVO productInfoVO, BigDecimal amount)
    {
        return insertBy(dataIdx, productInfoVO.getPdErc20Address(), null, null, null, null, null);
    }

    @Override
    public boolean insertProductPurchaseRemain(long dataIdx, PaymentQueueVO paymentQueueVO, MemberVO memberVO, ProductInfoVO productInfoVO, BigDecimal amount)
    {
        return insertBy(dataIdx, productInfoVO.getPdErc20Address(), null, null, null, null, null);
    }

    @Override
    public boolean insertProductCancel(long dataIdx, ProductUserVO productUserVO, BigDecimal finalReturn, BigDecimal feeAmount, String feeRate)
    {
        return insertBy(dataIdx, productUserVO.getProductInfoVO().getPdErc20Address(), productUserVO.getPuBaseAmount().toPlainString(), productUserVO.getPuInterestAmount().toPlainString(), feeAmount.toPlainString(), feeRate, null);
    }

    @Override
    public boolean insertDailyInterest(long dataIdx, ProductInfoVO productInfoVO, ProductUserVO productUserVO, BigDecimal dailyInterest, int interestIndex)
    {
        return insertBy(dataIdx, productUserVO.getProductInfoVO().getPdErc20Address(), productUserVO.getPuBaseAmount().toPlainString(), "" + interestIndex, null, null, null);
    }

    @Override
    public boolean insertLfDeposit(long dataIdx, PaymentQueueVO paymentQueueVO, TokenInfoVO tokenInfoVO)
    {
        return true;
    }

    @Override
    public boolean insertLfUnlock(long dataIdx, LfUserVO lfUserVO)
    {
        return true;
    }
}