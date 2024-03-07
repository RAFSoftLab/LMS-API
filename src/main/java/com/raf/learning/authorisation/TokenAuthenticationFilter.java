package com.raf.learning.authorisation;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class TokenAuthenticationFilter implements Filter {
    private final String validToken = "L2aTA643Z0UJ43bIdBymFExVbpqZg7v5QJafYh6KFRjl04eV6w4TtdppkX41hEwo"; // Change this to your valid token

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String token = httpRequest.getHeader("Authorization");
        if (token == null || !token.equals("Bearer " + validToken)) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        chain.doFilter(request, response);
    }
}
