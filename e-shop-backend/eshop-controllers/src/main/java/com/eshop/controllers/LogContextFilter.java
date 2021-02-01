package com.eshop.controllers;

import org.apache.logging.log4j.ThreadContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@Component
@Order(1)
public class LogContextFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        ThreadContext.put("ip", request.getRemoteAddr());
        ThreadContext.put("req", request.getMethod());
        ThreadContext.put("uri", request.getRequestURI());

        filterChain.doFilter(servletRequest,servletResponse);
    }
}
