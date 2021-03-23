package com.linkFlow.manager.common.service.impl;

import com.linkFlow.manager.common.config.CommonConstants;
import com.linkFlow.manager.common.model.define.OperatorState;
import com.linkFlow.manager.common.model.define.OperatorType;
import com.linkFlow.manager.common.model.vo.OperatorVO;
import com.linkFlow.manager.common.service.InitService;
import com.linkFlow.manager.common.service.MemberService;
import com.linkFlow.manager.common.service.OperatorService;
import com.linkFlow.manager.common.service.SysDataService;
import com.linkFlow.manager.common.util.CustomErc20Util;
import com.test32.common.StaticLoggerDef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class InitServiceImpl implements InitService
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired private OperatorService adminService;
    @Autowired private SysDataService sysDataService;
    @Autowired private CustomErc20Util customErc20Util;
    @Autowired private MemberService memberService;

    @PostConstruct
    public boolean checkAdminAccount()
    {
        logger.info(StaticLoggerDef.PREFIX_INIT + "InitService - start");
        boolean result = false;

        try
        {
            OperatorVO admin = adminService.selectById(CommonConstants.ADMIN_OPERATOR_ID);
            if (admin == null)
            {
                logger.warn("InitService - generate: OperatorVO");
                admin = new OperatorVO();
                admin.setOpId(CommonConstants.ADMIN_OPERATOR_ID);
                admin.setOpName(CommonConstants.ADMIN_OPERATOR_ID);
                admin.setOpPwd("1234");

                admin.setOpType(OperatorType.ADMIN);

                admin.setOpLevel(0);
                admin.setOpState(OperatorState.USE);

                result = adminService.insert(admin);
            }
            if( ! sysDataService.generateDefault())
            {
                logger.error("failed to: sysDataService.generateDefault()");
            }
        }
        catch (Exception e)
        {
            logger.error(e.toString());
            e.printStackTrace();
        }

        logger.info(StaticLoggerDef.PREFIX_INIT + "InitService - end");
        return result;
    }
}