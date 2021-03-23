package com.linkFlow.manager.common.dao;

import com.linkFlow.manager.common.model.vo.ProductUserVO;
import com.linkFlow.manager.common.model.vo.ProductUserVOForApi;
import com.linkFlow.manager.common.model.vo.ProductUserVOForSum;
import com.test32.common.generic.GenericDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProductUserDao extends GenericDao<ProductUserVO>
{
    List<ProductUserVOForApi> selectBySearchForApi(Map<String, Object> parameter);

    boolean appendBaseAmount(Map<String, Object> parameter);
    boolean appendInterestAmount(Map<String, Object> parameter);

    boolean startProduct(Map<String, Object> parameter);

    ProductUserVOForSum selectSumBySearch(Map<String, Object> parameter);
}