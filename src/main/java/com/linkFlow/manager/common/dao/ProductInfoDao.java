package com.linkFlow.manager.common.dao;

import com.linkFlow.manager.common.model.vo.ProductInfoVO;
import com.linkFlow.manager.common.model.vo.ProductInfoVOForApi;
import com.test32.common.generic.GenericDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProductInfoDao extends GenericDao<ProductInfoVO>
{
    List<ProductInfoVOForApi> selectBySearchForApi(Map<String, Object> parameter);

    boolean appendInterestAmount(Map<String, Object> parameter);
}