package com.linkFlow.manager.common.dao;

import com.linkFlow.manager.common.model.vo.NoticeVO;
import com.linkFlow.manager.common.model.vo.NoticeVOForApi;
import com.test32.common.generic.GenericDaoForBigInt;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface NoticeDao extends GenericDaoForBigInt<NoticeVO>
{
    List<NoticeVOForApi> selectBySearchForApi(Map<String, Object> parameter);
}