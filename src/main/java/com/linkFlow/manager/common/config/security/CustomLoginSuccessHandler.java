package com.linkFlow.manager.common.config.security;

import com.linkFlow.manager.common.config.CommonConstants;
import com.linkFlow.manager.common.config.ConfigDataManager;
import com.linkFlow.manager.common.model.vo.AccessIpVO;
import com.linkFlow.manager.common.model.vo.OperatorVO;
import com.linkFlow.manager.common.service.AccessIpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.linkFlow.manager.common.config.CommonConstants.SETUP_PAGE_PATH;
import static com.test32.common.StaticLoggerDef.PREFIX_INVALID_LOGIN;

@Service("customLoginSuccessHandler")
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired private ConfigDataManager configDataManager;
    @Autowired private AccessIpService accessIpService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException
    {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        OperatorVO operator = userDetails.getUserObject();
        request.getSession().setAttribute("loginUser", operator);

        request.getSession().setAttribute("SETUP_PAGE_PATH", SETUP_PAGE_PATH);

        request.getSession().setAttribute("EOS_ENDPOINT", configDataManager.getConfigData().getDefaultServerConfigData().getEosEndpoint());
        request.getSession().setAttribute("ETHERSCAN_ENDPOINT", configDataManager.getConfigData().getDefaultServerConfigData().getEtherscanEndpoint());

        request.getSession().setAttribute("ERC20_WITHDRAW_ACCOUNT", configDataManager.getConfigData().getDefaultServerConfigData().getErc20WithdrawAccount());


        request.getSession().setAttribute("CUSTOM_SITE_NAME", configDataManager.getConfigData().getDefaultServerConfigData().getSiteName());
        request.getSession().setAttribute("CUSTOM_SITE_URL", configDataManager.getConfigData().getDefaultServerConfigData().getSiteUrl());

        request.getSession().setAttribute("CUSTOM_SLACK_PAGE", configDataManager.getConfigData().getDefaultServerConfigData().getUrlSlackPage());



        String requestIp = request.getHeader("X-FORWARDED-FOR");
        if(StringUtils.isEmpty(requestIp))
            requestIp = request.getRemoteAddr();

        accessIpService.insertNotExist(requestIp);


        boolean isRestricted = true;
        AccessIpVO accessIpVO = accessIpService.selectByIp(requestIp);
        if(accessIpVO != null)
        {
            if(accessIpVO.getAcErrorCount() >= CommonConstants.MAX_LOGIN_ERROR_COUNT)
            {
                logger.error(PREFIX_INVALID_LOGIN + "restricted due to max error login count.  IP: " + requestIp);
                authentication.setAuthenticated(false);
                accessIpService.updateDate(requestIp);
            }
            else
            {
                isRestricted = false;
                accessIpService.decreaseErrorCount(requestIp);
            }
        }
        if(operator != null && ! isRestricted)
        {
        }
        response.sendRedirect(request.getContextPath() + "/dashboard");
    }
}