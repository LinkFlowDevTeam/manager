package com.linkFlow.manager.common.dao;

import com.linkFlow.manager.common.model.vo.SysDataVO;
import com.linkFlow.manager.common.model.vo.SysDataVOForApi;
import com.test32.common.generic.GenericDao;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysDataDao extends GenericDao<SysDataVO>
{
    SysDataVOForApi selectByIndexForApi(Integer idx);
}