package com.linkFlow.manager.admin.config;

import com.linkFlow.manager.common.config.CommonConstants;
import com.linkFlow.manager.common.model.BuildInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.annotation.Resource;
import java.util.Locale;

@Lazy
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.test32", "com.linkFlow.manager.admin"})
public class AdminWebConfig  implements WebMvcConfigurer
{
	@Resource
	private Environment environment;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());


	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry)
	{
		registry.addResourceHandler("/favicon.ico").addResourceLocations("/resources/images/");
		registry.addResourceHandler("/*.txt").addResourceLocations("/resources/");
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
		registry.addResourceHandler("/images/**").addResourceLocations("/resources/images/");
		registry.addResourceHandler("/views/**").addResourceLocations("/resources/views/");

		registry.addResourceHandler("swagger-ui.html") .addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**") .addResourceLocations("classpath:/META-INF/resources/webjars/");

		String uploadPath = CommonConstants.FILE_PATH_UPLOAD;
		logger.info("#UploadPath: " + uploadPath);
		registry.addResourceHandler("/upload/**").addResourceLocations("file:///" + uploadPath);
	}

	@Override
	public void addFormatters(FormatterRegistry formatterRegistry)
	{
	}

	@Bean
	public BuildInfo initBuildInfo()
	{
		BuildInfo buildInfo = new BuildInfo();
		try
		{
			String projectName = environment.getProperty("Build.Name");
			String version = environment.getProperty("Build.Version");
			String revision = environment.getProperty("Build.Revision");
			buildInfo.setProjectName(projectName);
			buildInfo.setVersion(version);
			buildInfo.setRevision(revision);
		}
		catch (Exception e)
		{
			logger.error(e.toString());
		}
		return buildInfo;
	}

	@Bean
	public ViewResolver viewResolver()
	{
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/view/admin/");
		resolver.setSuffix(".jsp");
		resolver.setOrder(2);
		return resolver;
	}

	@Bean
	public LocaleResolver localeResolver()
	{
		CookieLocaleResolver resolver = new CookieLocaleResolver();
		resolver.setCookieName("lang");
		resolver.setDefaultLocale(new Locale("en_US"));
		return resolver;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry)
	{
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("lang");
		registry.addInterceptor(localeChangeInterceptor);
	}

	@Bean
	public MessageSource messageSource()
	{
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("resources/messages/messages");
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setCacheSeconds(30);

		return messageSource;
	}

	@Bean
	public MultipartResolver multipartResolver() {
		CommonsMultipartResolver resolver = new CommonsMultipartResolver();
		resolver.setMaxInMemorySize(10000000);
		resolver.setMaxUploadSize(10000000);
		return resolver;
	}
}