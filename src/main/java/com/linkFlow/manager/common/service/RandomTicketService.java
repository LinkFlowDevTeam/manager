package com.linkFlow.manager.common.service;

import com.linkFlow.manager.common.model.vo.RandomTicketVO;
import com.test32.common.generic.GenericServiceForBigInt;

public interface RandomTicketService extends GenericServiceForBigInt<RandomTicketVO>
{
    int STATE_NONE = 0;
    int STATE_ACTIVE = 1;
    int STATE_EXPIRED = 2;

    RandomTicketVO generate(String from, String key);
    boolean expire(Long rtIdx);
    boolean expireByKey(String key);
    boolean deleteOverSecond(int second);
    boolean deleteExpired();
}