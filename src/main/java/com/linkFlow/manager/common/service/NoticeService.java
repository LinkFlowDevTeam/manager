package com.linkFlow.manager.common.service;

import com.linkFlow.manager.common.model.vo.NoticeVO;
import com.test32.common.generic.GenericServiceForBigInt;
import com.test32.common.paging.PagingData;

import java.util.Map;

public interface NoticeService extends GenericServiceForBigInt<NoticeVO>
{
    int STATE_DISABLE = 0;
    int STATE_ENABLE = 1;
    PagingData selectPagingBySearchForApi(Map<String, Object> parameter);
}