package com.movieworld.movieboard.Filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginLimitFilter extends GenericFilterBean {
    private final int MAX_ATTEMPTS=5;
    private final Map<String, Integer> attempts=new HashMap<>();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        String username = req.getParameter("username");

        if (username != null) {
            //Integer은 wrapper 클래스.
            Integer loginAttempts = attempts.get(username);

            if (loginAttempts == null) loginAttempts = 0;
            loginAttempts++;
            attempts.put(username, loginAttempts);

            if (loginAttempts >= MAX_ATTEMPTS) {
                HttpServletResponse res = (HttpServletResponse) response;
                res.setStatus(HttpServletResponse.SC_FORBIDDEN);
                res.getWriter().println("Too many login attempts");
                return;
            }
        }
        System.out.println("loginlimitfilter is running...\n");
        chain.doFilter(request, response);
    }

}
