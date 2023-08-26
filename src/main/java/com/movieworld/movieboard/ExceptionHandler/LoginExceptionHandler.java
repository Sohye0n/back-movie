package com.movieworld.movieboard.ExceptionHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginExceptionHandler extends SimpleUrlAuthenticationFailureHandler {
    private final int MAX_ATTEMPTS=5;
    private final Map<String, Integer> attempts=new HashMap<>();


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        String username = req.getParameter("id");
        Integer loginAttempts=null;

        if (username != null) {
            //Integer은 wrapper 클래스.
            loginAttempts = attempts.get(username);

            if (loginAttempts == null) loginAttempts = 0;
            loginAttempts++;
            attempts.put(username, loginAttempts);
        }
        System.out.println("usename:");
        System.out.println(username);
        System.out.println("authenticationfailurehandler is running...");

        String errorMessage;
        int errorNumber;

        if (loginAttempts >= MAX_ATTEMPTS) {
            errorNumber=1;
            //errorMessage="로그인 횟수가 너무 많습니다.";
        } else if(exception instanceof BadCredentialsException) {
            errorNumber=2;
            //errorMessage = "아이디 또는 비밀번호가 맞지 않습니다. 다시 확인해주세요.";
        } else if (exception instanceof InternalAuthenticationServiceException) {
            errorNumber=3;
            //errorMessage = "내부 시스템 문제로 로그인 요청을 처리할 수 없습니다. 관리자에게 문의하세요. ";
        } else if (exception instanceof UsernameNotFoundException) {
            errorNumber=4;
            //errorMessage = "존재하지 않는 계정입니다. 회원가입 후 로그인해주세요.";
        } else if (exception instanceof AuthenticationCredentialsNotFoundException) {
            errorNumber=5;
            //errorMessage = "인증 요청이 거부되었습니다. 관리자에게 문의하세요.";
        } else{
            errorNumber=6;
            //errorMessage = "알 수 없는 오류로 로그인 요청을 처리할 수 없습니다. 관리자에게 문의하세요.";
        }

        //errorMessage = URLEncoder.encode(errorMessage, "UTF-8"); /* 한글 인코딩 깨진 문제 방지 */
        setDefaultFailureUrl("/login?error="+errorNumber);
        super.onAuthenticationFailure(request, response, exception);
    }
}
