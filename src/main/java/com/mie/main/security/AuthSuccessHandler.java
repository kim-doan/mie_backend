package com.mie.main.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
/**
 * 
 * 인증 성공, 인증 실패, 로그 아웃시, 적절한 HTTP Response를 보낼 수 있도록, 몇가지 Handler를 Extend 한다.
 * - 인증 성공 시 : AuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler
 *
 */
@Component
public class AuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_OK);

    }

}