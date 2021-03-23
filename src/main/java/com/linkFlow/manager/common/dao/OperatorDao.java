package com.linkFlow.manager.common.dao;

import com.linkFlow.manager.common.model.vo.MemberVOForApi;
import com.linkFlow.manager.common.model.vo.OperatorVO;
import com.test32.common.generic.GenericDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface OperatorDao extends GenericDao<OperatorVO>
{
    List<OperatorVO> selectChildList(Map<String, Object> searchMap);
    Integer countBySearchForTreeView(Map<String, Object> searchMap);

    boolean updateOperatorStateByParent(Map<String, Object> searchMap);

    Integer selectParentIdxByIndex(Integer idx);
    OperatorVO selectParentByIndex(Integer idx);

    List<MemberVOForApi> selectBySearchForApi(Map<String, Object> searchMap);

    boolean addPointToOperator(Map<String, Object> searchMap);
    boolean subtractPointFromOperator(Map<String, Object> searchMap);
}