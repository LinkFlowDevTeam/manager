package com.linkFlow.manager.common.service;

import com.linkFlow.manager.common.model.vo.CommonWorkQueueVO;
import com.test32.common.generic.GenericServiceForBigInt;

public interface CommonWorkQueueService extends GenericServiceForBigInt<CommonWorkQueueVO>
{
    int STATE_NONE = 0;
    int STATE_SUCCESS = 1;
    int STATE_ERROR = 2;
    int STATE_CHECK_REQUIRED = 3;


    int TYPE_MEMBER_SNAPSHOT = 1;
    int TYPE_ERC20_REFRESH = 2;


    int EXECUTE_MODE_RUN_MULTIPLE_ABLE = 1;
    int EXECUTE_MODE_RUN_ONCE = 2;

    void runScheduledDefault();
    void runScheduledMemberStatistic();

    boolean executeItem(CommonWorkQueueVO entity);

    boolean insertMemberSnapshot(Long mbIdx);
    boolean insertErc20AddressTokenRefresh(String erc20Address, String symbol);

    //CommonWorkQueueVO generateMemberSnapshot(Long mbIdx);
}