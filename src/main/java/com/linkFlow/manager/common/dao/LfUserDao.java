package com.linkFlow.manager.common.dao;

import com.linkFlow.manager.common.model.vo.LfUserVO;
import com.linkFlow.manager.common.model.vo.LfUserVOForApi;
import com.linkFlow.manager.common.model.vo.LfUserVOForSum;
import com.test32.common.generic.GenericDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface LfUserDao extends GenericDao<LfUserVO>
{
    List<LfUserVOForApi> selectBySearchForApi(Map<String, Object> parameter);
    LfUserVOForSum selectSumBySearch(Map<String, Object> parameter);
}