package com.linkFlow.manager.common.config.security;

import com.linkFlow.manager.common.config.ConfigDataManager;
import com.linkFlow.manager.common.model.ReturnCode;
import com.linkFlow.manager.common.service.AccessIpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.test32.common.StaticLoggerDef.PREFIX_INVALID_LOGIN;

@Service
public class CustomLoginFailHandler implements AuthenticationFailureHandler
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ConfigDataManager configDataManager;
    @Autowired
    private AccessIpService accessIpService;
    final String LOGIN_ID = "j_username";
    final String LOGIN_PASSWORD = "j_password";

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException aException) throws IOException
    {
        String requestIp = request.getHeader("X-FORWARDED-FOR");
        if(StringUtils.isEmpty(requestIp))
            requestIp = request.getRemoteAddr();

        String loginID = request.getParameter(LOGIN_ID);
        String loginPassword = request.getParameter(LOGIN_PASSWORD);
        String failMessage = aException.getMessage();

        logger.error(PREFIX_INVALID_LOGIN + "ID: " + loginID + " IP: " + requestIp);


        accessIpService.insertNotExist(requestIp);
        accessIpService.increaseErrorCount(requestIp);
        accessIpService.updateDate(requestIp);

        request.setCharacterEncoding("UTF-8");

        request.setAttribute(LOGIN_ID, loginID);
        request.setAttribute(LOGIN_PASSWORD, loginPassword);

        if(ReturnCode.ACCOUNT_BLOCKED.getMessage().equalsIgnoreCase(failMessage))
        {
        }
        else
        {
            logger.error("authentication failed - id:" + loginID + " pwd:" + loginPassword + " message:" + failMessage);
            failMessage = "";
        }

        request.setAttribute("message", failMessage);
        request.getSession().setAttribute("message", failMessage);
        response.sendRedirect(request.getContextPath() + "/");
    }

}
