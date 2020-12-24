package com.asafalima.websocket.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Asaf Alima
 */
public class AuthenticationFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOGGER.info("Initializing filter, config: {}", filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        LOGGER.info("Checking client, request: {}", request);

        if ((request instanceof HttpServletRequest) && (response instanceof HttpServletResponse)) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;

            if (!httpServletRequest.getServletPath().equals("/echo")) {
                chain.doFilter(request, response);
                return;
            }

            Cookie[] cookies = httpServletRequest.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("accessToken") && cookie.getValue().equals("top-secret-token")) {
                        LOGGER.info("Finish checking client - client is authenticated");
                        chain.doFilter(request, response);
                        return;
                    }
                }
            }

            LOGGER.warn("Finish checking client - client is unauthenticated !");
            httpServletResponse.setStatus(401);
            httpServletResponse.getWriter().print("Invalid token !");
        }
    }

    @Override
    public void destroy() {

    }

}
