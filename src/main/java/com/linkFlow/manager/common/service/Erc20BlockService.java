package com.linkFlow.manager.common.service;


import com.linkFlow.manager.common.model.vo.Erc20BlockVO;
import com.test32.common.generic.GenericServiceForBigInt;

import java.util.Map;

public interface Erc20BlockService extends GenericServiceForBigInt<Erc20BlockVO>
{
    int STATE_NONE = 0;
    int STATE_PROCESSED = 1;
    int STATE_PENDING = 2;

    boolean insertNotExist(Erc20BlockVO erc20BlockVO);
    Erc20BlockVO getLastFinishedBlock();
    Erc20BlockVO getWorkingBlock();
    Erc20BlockVO selectByBlockNumber(long blockNumber);

    boolean updateByBlockNumber(Erc20BlockVO erc20BlockVO);
    Long countBySearch(Map<String, Object> parameter);
}