package com.linkFlow.manager;

import com.linkFlow.manager.api.config.ApiWebConfig;
import com.linkFlow.manager.common.config.ConfigDataManager;
import com.test32.common.StaticLoggerDef;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

@EnableScheduling
@SpringBootApplication(exclude = {MybatisAutoConfiguration.class})
@MapperScan(basePackages = "com.linkFlow.manager.common.dao")
public class LinkFlowApplication extends SpringBootServletInitializer implements ServletContextAware, ServletContextListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private ServletContext servletContext;

    public void setServletContext(ServletContext servletContext)
    {
        this.servletContext = servletContext;
    }

    @Bean
    public ConfigDataManager loadConfigData()
    {
        if (servletContext == null)
            logger.error("#servletContext is null");
        return new ConfigDataManager(servletContext.getVirtualServerName(), servletContext.getContextPath());
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(LinkFlowApplication.class);
    }

    @Bean
    public ServletRegistrationBean apiV()
    {
        DispatcherServlet dispatcherServlet = new DispatcherServlet();

        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
        applicationContext.register(ApiWebConfig.class);
        dispatcherServlet.setApplicationContext(applicationContext);

        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(dispatcherServlet, "/api/*");
        servletRegistrationBean.setName("api-v1");
        return servletRegistrationBean;
    }

    @Override
    public void contextInitialized(ServletContextEvent sce)
    {
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce)
    {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        // Loop through all drivers
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            if (driver.getClass().getClassLoader() == cl) {
                // This driver was registered by the webapp's ClassLoader, so deregister it:
                try {
                    logger.info("Deregistering JDBC driver {}", driver);
                    DriverManager.deregisterDriver(driver);
                }
                catch (SQLException ex) {
                    logger.error("Error deregistering JDBC driver {}", driver, ex);
                }
            }
            else {
                // driver was not registered by the webapp's ClassLoader and may be in use elsewhere
                logger.trace("Not deregistering JDBC driver {} as it does not belong to this webapp's ClassLoader", driver);
            }
        }
        logger.info(StaticLoggerDef.PREFIX_INIT + " contextDestroyed end");
    }

    public static void main(String[] args) {
        SpringApplication.run(LinkFlowApplication.class, args);
    }

}
