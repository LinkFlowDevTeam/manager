package com.linkFlow.manager.common.service.impl;

import com.linkFlow.manager.common.config.ConfigDataManager;
import com.linkFlow.manager.common.dao.OperatorDao;
import com.linkFlow.manager.common.model.define.OperatorState;
import com.linkFlow.manager.common.model.vo.OperatorVO;
import com.linkFlow.manager.common.service.OperatorService;
import com.test32.common.paging.PagingData;
import com.test32.common.paging.PagingManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional("apiTransactionManager")
public class OperatorServiceImpl implements OperatorService
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired private ConfigDataManager configDataManager;
    @Autowired private BCryptPasswordEncoder passwordEncoder;
    @Autowired private OperatorDao operatorDao;

    @Override
    public boolean checkPassword(Integer opIdx, String password)
    {
        OperatorVO operatorVO = select(opIdx);
        if(operatorVO != null)
        {
            String priorPassword = operatorVO.getOpPwd();
            String newEncoded = passwordEncoder.encode(password);
            if(priorPassword != null)
            {
                return passwordEncoder.matches(password, priorPassword);
            }
        }
        return false;
    }

    @Override
    public OperatorVO selectById(String operatorId)
    {
        OperatorVO result = null;

        Map<String, Object> searchMap = new HashMap<>();
        searchMap.put("opId", operatorId);

        List<OperatorVO> operators = operatorDao.selectBySearch(searchMap);

        if (operators != null && operators.size() != 0)
        {
            result = operators.get(0);
        }
        return result;
    }

    @Override
    public Integer selectParentIdxByIndex(int childIdx)
    {
        return operatorDao.selectParentIdxByIndex(childIdx);
    }

    @Override
    public OperatorVO selectMasterByChildOpIdx(int childIdx)
    {
        return operatorDao.selectParentByIndex(childIdx);
    }

    @Override
    public boolean changeOperatorPassword(String operatorId, String changePassword)
    {
        boolean result = false;

        OperatorVO operator = selectById(operatorId);

        if (operator != null)
        {
            changePassword = passwordEncoder.encode(changePassword);
            operator.setOpPwd(changePassword);

            update(operator);
            result = true;
        }

        return result;
    }


    @Override
    public boolean insert(OperatorVO entity)
    {
        if (StringUtils.isEmpty(entity.getOpPrivateKey()))
        {
            String privateKey = "1234";
            entity.setOpPrivateKey(privateKey);
        }

        String inputPassword = entity.getOpPwd();
        String encodingPassword = passwordEncoder.encode(inputPassword);
        entity.setOpPwd(encodingPassword);

        return operatorDao.insert(entity);
    }

    @Override
    public OperatorVO select(Integer index)
    {
        return operatorDao.selectByIndex(index);
    }

    @Override
    public List<OperatorVO> selectAll()
    {
        return operatorDao.selectAll();
    }

    @Override
    public List<OperatorVO> selectBySearch(Map<String, Object> parameter)
    {
        return operatorDao.selectBySearch(parameter);
    }

    @Override
    public PagingData selectPagingBySearch(Map<String, Object> parameter)
    {
        String pageNumberStr = (String) parameter.get("pageNo");
        String pageSizeStr = (String) parameter.get("pageSize");
        int pageNumber = StringUtils.isEmpty(pageNumberStr) ? 1 : Integer.valueOf(pageNumberStr);
        int pageSize = StringUtils.isEmpty(pageSizeStr) ? 20 : Integer.valueOf(pageSizeStr);
        int totalRecode = operatorDao.countBySearch(parameter);

        PagingData pagingData = PagingManager.getPagingList(pageNumber, Long.valueOf("" + totalRecode), pageSize, 10);
        parameter.put("startRow", pagingData.getStartRow());
        parameter.put("rowCount", pagingData.getPageSize());

        List<OperatorVO> dataList = selectBySearch(parameter);
        pagingData.setObject(dataList);
        pagingData.setCurrentPageRowCount(dataList.size());

        return pagingData;
    }

    @Override
    public PagingData selectPagingBySearchForTreeView(Map<String, Object> parameter)
    {
        OperatorVO searchOp = null;
        if(parameter.containsKey("search"))
        {
            String searchId = (String)parameter.get("search");
            if( ! StringUtils.isEmpty(searchId))
            {
                searchOp = selectById(searchId);
                if(searchOp != null)
                {
                    parameter.put("parentOpIdx", searchOp.getOpIdx());
                    parameter.remove("queryColumn");
                    parameter.remove("search");

                    searchOp.setTreeItem("" + searchOp.getOpIdx());
                    searchOp.setTreeLevel(1);
                    searchOp.setTreeRnum("0");
                }
            }
        }

        String pageNumberStr = (String) parameter.get("pageNo");
        String pageSizeStr = (String) parameter.get("pageSize");
        int pageNumber = StringUtils.isEmpty(pageNumberStr) ? 1 : Integer.valueOf(pageNumberStr);
        int pageSize = StringUtils.isEmpty(pageSizeStr) ? 20 : Integer.valueOf(pageSizeStr);
        int totalRecode = operatorDao.countBySearchForTreeView(parameter);

        PagingData pagingData = PagingManager.getPagingList(pageNumber, Long.valueOf("" + totalRecode), pageSize, 10);
        parameter.put("startRow", pagingData.getStartRow());
        parameter.put("rowCount", pagingData.getPageSize());

        List<OperatorVO> dataList = selectChildList(parameter);
        if(searchOp != null)
        {
            for(OperatorVO op : dataList)
                op.setTreeItem(" " + op.getTreeItem());
            if(pageNumber == 1)
                dataList.add(0, searchOp);
        }

        pagingData.setObject(dataList);
        pagingData.setCurrentPageRowCount(dataList.size());

        return pagingData;
    }

    @Override
    public boolean update(OperatorVO entity)
    {
        if( ! StringUtils.isEmpty(entity.getOpPwd()))
        {
            String encodingPassword = passwordEncoder.encode(entity.getOpPwd());
            entity.setOpPwd(encodingPassword);
        }
        else
        {
            entity.setOpPwd(null);
        }
        return operatorDao.update(entity);
    }

    @Override
    public boolean delete(Integer index)
    {
        return operatorDao.deleteByIndex(index);
    }

    @Override
    public List<OperatorVO> selectChildList(Map<String, Object> searchMap)
    {
        return operatorDao.selectChildList(searchMap);
    }

    @Override
    public List<OperatorVO> selectChildListByParent(int parentOpIdx)
    {
        Map<String, Object> searchMap = new HashMap<>();
        searchMap.put("parentOpIdx", parentOpIdx);

        return selectChildList(searchMap);
    }

    @Override
    public List<OperatorVO> selectChildListWithDepth(int parentOpIdx, int childDepth)
    {
        Map<String, Object> searchMap = new HashMap<>();
        searchMap.put("parentOpIdx", parentOpIdx);
        searchMap.put("searchLevel", childDepth);

        return selectChildList(searchMap);
    }

    @Override
    public boolean isChild(int parentOpIdx, int childOpIdx, boolean isDirectChild)
    {
        Map<String, Object> searchMap = new HashMap<>();
        searchMap.put("parentOpIdx", parentOpIdx);
        searchMap.put("childOpIdx", childOpIdx);
        if(isDirectChild)
            searchMap.put("searchLevel", 1);

        List<OperatorVO> list = selectChildList(searchMap);
        return (list.size() > 0);
    }

    @Override
    public boolean updateOperatorStateByParent(int parentOpIdx, OperatorState operatorState)
    {
        List<OperatorVO> childList = selectChildListByParent(parentOpIdx);
        if(childList.size() > 0)
        {
            List<Integer> opIdxList = new ArrayList<>();
            for(int i = 0; i < childList.size(); i++)
            {
                opIdxList.add(childList.get(i).getOpIdx());
            }
            Map<String, Object> searchMap = new HashMap<>();
            searchMap.put("opState", operatorState);
            searchMap.put("opIdxList", opIdxList);
            return operatorDao.updateOperatorStateByParent(searchMap);
        }
        else
        {
            return true;
        }
    }
}