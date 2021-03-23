package com.linkFlow.manager.api.config;

import com.linkFlow.manager.api.interceptor.ApiLogInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Lazy;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.*;

@EnableWebMvc
@Lazy
@EnableTransactionManagement
@ComponentScan(basePackages = {"com.test32", "com.linkFlow.manager.api"})
public class ApiWebConfig implements WebMvcConfigurer
{
    @Autowired private ApiLogInterceptor apiLogInterceptor;


    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer)
    {
        configurer.defaultContentType(MediaType.APPLICATION_JSON);
        configurer.mediaType("json", MediaType.APPLICATION_JSON);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(apiLogInterceptor);
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer)
    {
        configurer.enable();
    }

    @Override
    public void addFormatters(FormatterRegistry formatterRegistry)
    {
    }
}