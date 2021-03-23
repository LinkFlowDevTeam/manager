package com.linkFlow.manager.common.config.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class CustomAccessDeniedHandler implements AccessDeniedHandler
{
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException,
            ServletException
	{
		try
        {
            String requestIp = request.getHeader("X-FORWARDED-FOR");
            if(StringUtils.isEmpty(requestIp))
                requestIp = request.getRemoteAddr();

            String errorMessage = requestIp;

            String reqUri = request.getRequestURI();
            if( ! StringUtils.isEmpty(reqUri))
                errorMessage = errorMessage + ", " + reqUri;
            logger.error("accessDenied : " + errorMessage);
        }
        catch (Exception e)
        {
            logger.error(e.toString());
        }
		request.getRequestDispatcher("/accessDenied").forward(request, response);
	}

}
