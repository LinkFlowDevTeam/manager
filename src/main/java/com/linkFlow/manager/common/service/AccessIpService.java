package com.linkFlow.manager.common.service;

import com.linkFlow.manager.common.model.vo.AccessIpVO;
import com.test32.common.generic.GenericServiceForBigInt;

public interface AccessIpService extends GenericServiceForBigInt<AccessIpVO>
{
	int MAX_IP_LENGTH = 45;

	AccessIpVO selectByIp(String ip);
	boolean insertNotExist(String ip);
	boolean increaseErrorCount(String ip);
	boolean decreaseErrorCount(String ip);
	boolean updateDate(String ip);
	boolean resetErrorCount(String ip);
	boolean resetErrorCountByIdx(int idx);
	boolean resetErrorCountAll();

}