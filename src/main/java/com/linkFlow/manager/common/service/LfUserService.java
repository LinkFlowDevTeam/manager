package com.linkFlow.manager.common.service;

import com.linkFlow.manager.common.model.vo.LfUserVO;
import com.linkFlow.manager.common.model.vo.LfUserVOForApi;
import com.linkFlow.manager.common.model.vo.LfUserVOForSum;
import com.test32.common.generic.GenericService;

import java.util.List;
import java.util.Map;

public interface LfUserService extends GenericService<LfUserVO>
{
    int STATE_ACTIVE = 1;
    int STATE_EXPIRED = 2;

    Integer countBySearch(Map<String, Object> parameter);
    LfUserVOForSum selectActiveSum(Long mbIdx);
    List<LfUserVOForApi> selectBySearchForApi(Map<String, Object> parameter);

    boolean expireItem(LfUserVO lfUserVO);
}