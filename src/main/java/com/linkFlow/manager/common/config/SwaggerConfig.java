package com.linkFlow.manager.common.config;

import com.linkFlow.manager.api.interceptor.ApiLogInterceptor;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class SwaggerConfig implements WebMvcConfigurer
{
    @Autowired private ApiLogInterceptor apiLogInterceptor;

    String version = "V1";
    String title = "test title";


    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .setGroup("springshop-public")
                .packagesToScan("com.linkFlow.manager.api.web")
                .pathsToMatch("/v1/**")
                .build();
    }

    @Bean
    public OpenAPI api(){
        return new OpenAPI()
                .info(
                    new Info().title("API 타이틀")
                    .description("API 상세소개 및 사용법 등")
                    .version("1.0")
                )
                ;

    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer)
    {
        configurer.defaultContentType(MediaType.APPLICATION_JSON);
        configurer.mediaType("json", MediaType.APPLICATION_JSON);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {

    }

}