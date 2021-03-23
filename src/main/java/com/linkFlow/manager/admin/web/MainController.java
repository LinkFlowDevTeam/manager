package com.linkFlow.manager.admin.web;

import com.linkFlow.manager.common.config.ConfigDataManager;
import com.linkFlow.manager.common.model.BaseResponse;
import com.linkFlow.manager.common.model.ReturnCode;
import com.linkFlow.manager.common.service.MemberService;
import com.linkFlow.manager.common.util.CustomComponentUtil;
import com.test32.common.util.CommonDateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/")
@Controller
public class MainController
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired private ConfigDataManager configDataManager;
    @Autowired private MemberService memberService;
    @Autowired private CustomComponentUtil customComponentUtil;

    @RequestMapping(method = RequestMethod.GET)
	public String showMainPage(HttpServletRequest request, HttpServletResponse response, Model model, Principal principal)
	{
        String resultPage;
        String requestIp = request.getHeader("X-FORWARDED-FOR");
        if(StringUtils.isEmpty(requestIp))
            requestIp = request.getRemoteAddr();
        request.getSession().setAttribute("USER_REQUEST_IP", requestIp);
        resultPage = "login-dev";

        request.getSession().setAttribute("CUSTOM_SITE_NAME", configDataManager.getConfigData().getDefaultServerConfigData().getSiteName());
        request.getSession().setAttribute("CUSTOM_SITE_URL", configDataManager.getConfigData().getDefaultServerConfigData().getSiteUrl());

		return resultPage;
	}


	@RequestMapping(value = {"/dashboard", "/dashBoard"}, method = RequestMethod.GET)
	public String showDashboard(Model model)
	{
	    String resultPage;
//		model.addAttribute(buildInfo);
        resultPage = "/dashboard";

        Date today = new Date();
        SimpleDateFormat resultFormat = customComponentUtil.newSimpleDateFormatFull();
        SimpleDateFormat resultFormatHalf = customComponentUtil.newSimpleDateFormatHalf();
        Date yesterday = CommonDateUtil.addDay(today, -1);
//
        model.addAttribute("dashUserToday", memberService.countNewUser(today));
        model.addAttribute("dashUserYesterday", memberService.countNewUser(yesterday));
        model.addAttribute("dashUserAll", memberService.countBySearch(null));
        {
            Map<String, Object> activeUserCondition = new HashMap<>();
            activeUserCondition.put("mbState", "0");
            model.addAttribute("dashActiveUserAll", memberService.countBySearch(activeUserCondition));
        }
        return resultPage;
	}

    @RequestMapping(value = {"/checkAlive"}, method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse checkAlive()
    {
        BaseResponse response = new BaseResponse();
        response.setReturnCode(ReturnCode.SUCCESS);
        response.setDescription(response.getReturnCode().getMessage());
        return response;
    }
}