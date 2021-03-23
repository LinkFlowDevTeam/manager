package com.linkFlow.manager.api.controllerAdvice;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkFlow.manager.common.config.ConfigDataManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class ResponseLogAdvice implements ResponseBodyAdvice<Object>
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired private ConfigDataManager configDataManager;

    @Override
    public boolean supports(MethodParameter methodParameter,
                            Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o,
                                  MethodParameter methodParameter,
                                  MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> aClass,
                                  ServerHttpRequest serverHttpRequest,
                                  ServerHttpResponse serverHttpResponse)
    {
        if((configDataManager.getConfigData().getDefaultServerConfigData().getPrintResponseLog() != null && configDataManager.getConfigData().getDefaultServerConfigData().getPrintResponseLog()))
        {
            if (serverHttpRequest instanceof ServletServerHttpRequest && serverHttpResponse instanceof ServletServerHttpResponse)
            {
                try
                {
                    String requestUri = ((ServletServerHttpRequest) serverHttpRequest).getServletRequest().getRequestURI();
                    if( ! requestUri.contains("/api/v1/token/chart") && ! requestUri.contains("/api/v1/token/price") && ! requestUri.contains("/api/v1/user/token"))
                    {
                        ObjectMapper mapper = new ObjectMapper();
                        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                        logger.info(mapper.writeValueAsString(o));
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        return o;
    }
}