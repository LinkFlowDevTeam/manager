package com.linkFlow.manager.admin.web;

import com.linkFlow.manager.common.model.vo.OperatorVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Order(value = 99)
public class AdminExceptionHandler
{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @ExceptionHandler(MissingServletRequestParameterException.class)
    public String handleMyException(Exception exception, HttpServletRequest request)
    {
        return "error/404ErrorPage";
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handle(Exception ex, HttpServletRequest request)
    {
        return "error/404ErrorPage";
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public String messageNotReadableHandle(Exception ex, HttpServletRequest request)
    {
        return "error/400ErrorPage";
    }

    @ExceptionHandler(TypeMismatchException.class)
    public String typeMismatchHandle(Exception ex)
    {
        return "error/400ErrorPage";
    }

    @ExceptionHandler(AccessDeniedException.class)
    public String accessDeniedExceptionHandle(Exception ex, HttpServletRequest request)
    {
        try {
            String requestIp = request.getHeader("X-FORWARDED-FOR");
            if (StringUtils.isEmpty(requestIp))
                requestIp = request.getRemoteAddr();

            String errorMessage = requestIp;

            String reqUri = request.getRequestURI();
            if (!StringUtils.isEmpty(reqUri))
                errorMessage = errorMessage + ", " + reqUri;

            OperatorVO operator = (OperatorVO) request.getSession().getAttribute("loginUser");
            if (operator != null)
                errorMessage = errorMessage + ", opId: " + operator.getOpId();
            logger.error("accessDenied : " + errorMessage);
        }
        catch (Exception subEx) {
            logger.error(subEx.toString());
        }
        return "error/403ErrorPage";
    }

    @ExceptionHandler(Exception.class)
    public String exceptionHandle(Exception e, Model model)
    {
        logger.error("ControllerAdvice/Exception");
        logger.error(e.getMessage());
        model.addAttribute("errorMessage", e.getMessage());
        e.printStackTrace();
        return "error/500ErrorPage";
    }
}