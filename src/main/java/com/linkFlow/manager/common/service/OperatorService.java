package com.linkFlow.manager.common.service;

import com.linkFlow.manager.common.model.define.OperatorState;
import com.linkFlow.manager.common.model.vo.OperatorVO;
import com.test32.common.generic.GenericService;
import com.test32.common.paging.PagingData;

import java.util.List;
import java.util.Map;

public interface OperatorService extends GenericService<OperatorVO>
{
    boolean checkPassword(Integer opIdx, String password);

    OperatorVO selectById(String operatorId);

    Integer selectParentIdxByIndex(int childIdx);
    OperatorVO selectMasterByChildOpIdx(int childIdx);

    boolean changeOperatorPassword(String operatorId, String changePassword);

    PagingData selectPagingBySearchForTreeView(Map<String, Object> parameter);

    List<OperatorVO> selectChildList(Map<String, Object> searchMap);
    List<OperatorVO> selectChildListByParent(int parentOpIdx);
    List<OperatorVO> selectChildListWithDepth(int parentOpIdx, int childDepth);

    boolean isChild(int parentOpIdx, int childOpIdx, boolean isDirectChild);

    boolean updateOperatorStateByParent(int parentOpIdx, OperatorState operatorState);

//    List<TokenInfoVO> getUsableSymbolList(OperatorVO operator);
}
