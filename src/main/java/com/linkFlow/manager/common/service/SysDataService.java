package com.linkFlow.manager.common.service;

import com.linkFlow.manager.common.model.vo.SysDataVO;
import com.linkFlow.manager.common.model.vo.SysDataVOForApi;
import com.test32.common.generic.GenericService;

public interface SysDataService extends GenericService<SysDataVO>
{
    SysDataVO selectDefault();
    boolean generateDefault();
    SysDataVOForApi selectByIndexForApi();

//    boolean updateCheckGrade(String dateString);
//    boolean updateRewardStar(String dateString);
//    boolean updateRewardChildInterest(String dateString);
//    boolean updateRewardDividend(String dateString);
}