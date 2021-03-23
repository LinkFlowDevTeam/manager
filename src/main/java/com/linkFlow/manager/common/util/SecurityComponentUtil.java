package com.linkFlow.manager.common.util;

import com.linkFlow.manager.admin.ServiceException;
import com.linkFlow.manager.common.config.security.CustomUserDetails;
import com.linkFlow.manager.common.model.ReturnCode;
import com.linkFlow.manager.common.model.define.OperatorType;
import com.linkFlow.manager.common.model.vo.OperatorVO;
import com.linkFlow.manager.common.service.OperatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class SecurityComponentUtil
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OperatorService operatorService;

    public OperatorVO getLoginOperatorVO()
    {
        CustomUserDetails loginUser = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return loginUser.getUserObject();
    }

    public OperatorVO getLoginOperatorRenewed()
    {
        return operatorService.select(getLoginOperatorVO().getOpIdx());
    }
    public void returnOutOfAuthorityPage()
    {
        logger.error("out of authority");
        throw new ServiceException(ReturnCode.OUT_OF_AUTHORITY, ReturnCode.OUT_OF_AUTHORITY.getMessage());
    }

    public void returnErrorPage(ReturnCode returnCode, String detailMessage)
    {
        if(StringUtils.isEmpty(detailMessage))
            throw new ServiceException(returnCode, returnCode.getMessage());
        else
            throw new ServiceException(returnCode, returnCode.getMessage() + " - " + detailMessage);
    }

    public boolean isSystemAdmin()
    {
        return (getLoginOperatorVO().getOpType() == OperatorType.ADMIN);
    }
}