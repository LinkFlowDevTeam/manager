package com.linkFlow.manager.common.dao;


import com.linkFlow.manager.common.model.vo.CommonQueryVO;
import com.test32.common.generic.GenericDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface CommonQueryDao extends GenericDao<CommonQueryVO>
{
    Integer countTable(Map<String, Object> searchMap);
    boolean directQuery(Map<String, Object> searchMap);
    String selectNowString();
}