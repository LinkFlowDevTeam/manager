package com.linkFlow.manager.common.service;

import com.linkFlow.manager.common.model.vo.CommonTicketVO;
import com.test32.common.generic.GenericServiceForBigInt;

public interface CommonTicketService extends GenericServiceForBigInt<CommonTicketVO>
{
    int STATE_NONE = 0;
    int STATE_ACTIVE = 1;
    int STATE_EXPIRED = 2;


    CommonTicketVO generate(Long mbIdx);
    CommonTicketVO selectByRequestId(String genId);
    boolean checkSign(String genId, String sign);
    boolean updateDate(Long tkIdx);
    boolean increaseErrorCount(Long tkIdx);
    boolean expire(Long tkIdx);
//    CommonTicketVO generate(String userId);
//    boolean checkSign(String userId, String sign);
//    boolean isUserSignMatch(String userId, String sign);
//    boolean updateSign(Long tkIdx, String userId, String sign);
//    boolean updateDate(Long tkIdx, String userId, String sign);
    //boolean deleteByUpdateDate(Date updateDate);

    boolean deleteOverSecond(int second);
}