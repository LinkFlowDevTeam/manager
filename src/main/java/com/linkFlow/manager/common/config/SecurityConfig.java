package com.linkFlow.manager.common.config;

import com.linkFlow.manager.common.config.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalAuthentication
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    @Autowired private CustomUserDetailsService userDetailService;
    @Autowired private CustomLoginSuccessHandler loginSuccessHandler;
    @Autowired private CustomLoginFailHandler loginFailHandler;
    @Autowired private CustomAccessDeniedHandler accessDeniedHandler;

    @Override
	protected void configure(HttpSecurity httpSecurity) throws Exception
	{
		httpSecurity
            .authorizeRequests()
                .antMatchers("/robots.txt").permitAll()
                .antMatchers("/favicon.ico").permitAll()
                .antMatchers("/upload/**").permitAll()
                .antMatchers("/resources/**").permitAll()
                .antMatchers("/download/**").permitAll()
                .antMatchers("/management/feed/view/**").permitAll()
				.antMatchers("/api/**").permitAll()
                .antMatchers("/setup/**").hasAnyAuthority(SecurityRoleDef.RL_SYSTEM_ADMIN).anyRequest().authenticated()
                .and()
			.formLogin()
                .loginPage("/").permitAll()
                .loginProcessingUrl("/j_spring_security_check_member")
                .usernameParameter("j_username")
                .passwordParameter("j_password")
                .failureUrl("/?error=true")
                .successHandler(loginSuccessHandler)
                .failureHandler(loginFailHandler)
                .and()
            .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .and()
            .headers()
                .frameOptions().disable()
                .and()
            .csrf().disable().addFilterAfter(switchUserFilter(), FilterSecurityInterceptor.class)
                .httpBasic()
                .and()
            .rememberMe().key("wallet-remember-me")
                .rememberMeCookieName("wallet")
				.and()
            .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler);
	}

    @Bean
    @Override
    protected UserDetailsService userDetailsService()
    {
        return userDetailService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    @Bean(name = "myAuthenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception
    {
        return super.authenticationManagerBean();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    public SwitchUserFilter switchUserFilter() {
        SwitchUserFilter filter = new SwitchUserFilter();
        filter.setUserDetailsService(userDetailsService());
        filter.setUsernameParameter("u");
        filter.setSwitchUserUrl("/su");
        filter.setExitUserUrl("/sux");
        filter.setTargetUrl("/");
        return filter;
    }
}