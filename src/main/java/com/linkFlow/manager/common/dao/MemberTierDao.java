package com.linkFlow.manager.common.dao;

import com.linkFlow.manager.common.model.vo.MemberTierVO;
import com.linkFlow.manager.common.model.vo.MemberTierVOForApi;
import com.test32.common.generic.GenericDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MemberTierDao extends GenericDao<MemberTierVO>
{
    List<MemberTierVOForApi> selectBySearchForApi(Map<String, Object> parameter);
}