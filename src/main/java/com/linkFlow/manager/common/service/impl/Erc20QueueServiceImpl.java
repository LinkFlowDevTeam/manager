package com.linkFlow.manager.common.service.impl;

import com.linkFlow.manager.common.dao.Erc20QueueDao;
import com.linkFlow.manager.common.model.vo.Erc20QueueVO;
import com.linkFlow.manager.common.model.vo.Erc20TxVO;
import com.linkFlow.manager.common.service.Erc20QueueService;
import com.linkFlow.manager.common.service.Erc20TxService;
import com.test32.common.model.blockChain.erc20.jsonRpc.JsonRpcGetTransactionByHash;
import com.test32.common.paging.PagingData;
import com.test32.common.paging.PagingManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("erc20QueueService")
@Transactional("apiTransactionManager")
public class Erc20QueueServiceImpl implements Erc20QueueService
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired private Erc20QueueDao erc20QueueDao;
    @Autowired private Erc20TxService erc20TxService;

    @Override public boolean insert(Erc20QueueVO entity) { return erc20QueueDao.insert(entity); }
    @Override public Erc20QueueVO select(Long index) { return erc20QueueDao.selectByIndex(index); }
    @Override public List<Erc20QueueVO> selectAll() { return erc20QueueDao.selectAll(); }
    @Override public List<Erc20QueueVO> selectBySearch(Map<String, Object> parameter) { return erc20QueueDao.selectBySearch(parameter); }
    @Override public boolean update(Erc20QueueVO entity) { return erc20QueueDao.update(entity); }
    @Override public boolean delete(Long index) { return erc20QueueDao.deleteByIndex(index); }

    @Override
    public PagingData selectPagingBySearch(Map<String, Object> parameter)
    {
        String pageNumberStr = (String) parameter.get("pageNo");
        String pageSizeStr = (String) parameter.get("pageSize");
        int pageNumber = StringUtils.isEmpty(pageNumberStr) ? 1 : Integer.valueOf(pageNumberStr);
        int pageSize = StringUtils.isEmpty(pageSizeStr) ? 20 : Integer.valueOf(pageSizeStr);
        Long totalRecode = erc20QueueDao.countBySearch(parameter);

        PagingData pagingData = PagingManager.getPagingList(pageNumber, totalRecode, pageSize, 10);
        parameter.put("startRow", pagingData.getStartRow());
        parameter.put("rowCount", pagingData.getPageSize());

        List<Erc20QueueVO> dataList = selectBySearch(parameter);
        pagingData.setObject(dataList);
        pagingData.setCurrentPageRowCount(dataList.size());

        return pagingData;
    }


    @Override
    public boolean notifyTxInserted(Erc20TxVO erc20TxVO)
    {
        try
        {
            Map<String, Object> parameter = new HashMap<>();
            parameter.put("eqTxHash", erc20TxVO.getEtTxHash());
            parameter.put("eqChainState", Erc20QueueService.CHAIN_STATE_NONE);
            parameter.put("orderColumn", "eq_idx");
            parameter.put("order", "DESC");
            List<Erc20QueueVO> list = selectBySearch(parameter);
            if(list.size() > 1)
            {
                // just for warning
                logger.warn("duplicated txHash: " + erc20TxVO);
            }
            if(list.size() > 0)
            {
                Erc20QueueVO forUpdate = new Erc20QueueVO();
                forUpdate.setEqIdx(list.get(0).getEqIdx());
                forUpdate.setEqChainState(Erc20QueueService.CHAIN_STATE_INSERTED);

                return update(forUpdate);
            }
        }
        catch (Exception e)
        {
            logger.error(e.toString());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean notifyTxChecked(String txHash, JsonRpcGetTransactionByHash jsonRpcGetTransactionByHash)
    {
        try
        {
            Map<String, Object> parameter = new HashMap<>();
            parameter.put("eqTxHash", txHash);

            parameter.put("orderColumn", "eq_idx");
            parameter.put("order", "DESC");
            List<Erc20QueueVO> list = selectBySearch(parameter);
            if(list.size() > 1)
            {
                // just for warning
                logger.warn("duplicated txHash: " + txHash);
            }
            if(list.size() > 0)
            {
                Erc20QueueVO forUpdate = new Erc20QueueVO();
                forUpdate.setEqIdx(list.get(0).getEqIdx());

                if(jsonRpcGetTransactionByHash.getResult() == null)
                    forUpdate.setEqChainState(Erc20QueueService.CHAIN_STATE_TX_MISSING);
                else if( ! StringUtils.isEmpty(jsonRpcGetTransactionByHash.getResult().getBlockNumber()))
                {
                    forUpdate.setEqChainState(Erc20QueueService.CHAIN_STATE_TX_CONFIRMED);
                }
                else
                {
                    forUpdate.setEqChainState(Erc20QueueService.CHAIN_STATE_TX_PENDING);
                }

                return update(forUpdate);
            }
        }
        catch (Exception e)
        {
            logger.error(e.toString());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean notifyReceiptChecked(Long erc20TxVO_idx)
    {
        try
        {
            Erc20TxVO erc20TxVO = erc20TxService.select(erc20TxVO_idx);
            Map<String, Object> parameter = new HashMap<>();
            parameter.put("eqTxHash", erc20TxVO.getEtTxHash());
            parameter.put("orderColumn", "eq_idx");
            parameter.put("order", "DESC");
            List<Erc20QueueVO> list = selectBySearch(parameter);
            if(list.size() > 1)
            {
                // just for warning
                logger.warn("duplicated txHash: " + erc20TxVO);
            }
            if(list.size() > 0)
            {
                Erc20QueueVO forUpdate = new Erc20QueueVO();
                forUpdate.setEqIdx(list.get(0).getEqIdx());
                forUpdate.setEqChainState(Erc20QueueService.CHAIN_STATE_INSERTED);
                forUpdate.setEqChainMessage(erc20TxVO.getEtRcStatus());
                if(Erc20TxService.STATUS_RECEIPT_SUCCESS.equalsIgnoreCase(erc20TxVO.getEtRcStatus()))
                    forUpdate.setEqChainState(Erc20QueueService.CHAIN_STATE_RECEIPT_SUCCESS);
                else
                    forUpdate.setEqChainState(Erc20QueueService.CHAIN_STATE_RECEIPT_ERROR);

                forUpdate.setEqResponseDate(erc20TxVO.getEtUpdateDate());

                forUpdate.setEqGasUsed("" + erc20TxVO.getEtRcGasUsed());
                forUpdate.setEqGasLimit(erc20TxVO.getEtRcGasLimit());
                forUpdate.setEqStatus(erc20TxVO.getEtRcStatus());
                forUpdate.setEqBlockNumber(erc20TxVO.getEtBlockNumber());

                return update(forUpdate);
            }
        }
        catch (Exception e)
        {
            logger.error(e.toString());
            e.printStackTrace();
        }
        return false;
    }
}