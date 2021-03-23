package com.linkFlow.manager.api.interceptor;

import com.linkFlow.manager.common.config.ConfigDataManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Component("apiLogInterceptor")
public class ApiLogInterceptor extends HandlerInterceptorAdapter
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired private ConfigDataManager configDataManager;

    private String convertWithStream(Map<String, String[]> map) {
        return map.keySet().stream()
                .map(key -> key + "=" + Arrays.toString(map.get(key)) )
                .collect(Collectors.joining(", ", "{", "}"));
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
    {
        if((configDataManager.getConfigData().getDefaultServerConfigData().getPrintApiLog() != null && configDataManager.getConfigData().getDefaultServerConfigData().getPrintApiLog()))
        {
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception
    {
        super.afterCompletion(request, response, handler, ex);
    }
}