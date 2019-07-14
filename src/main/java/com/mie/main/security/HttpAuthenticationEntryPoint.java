package com.mie.main.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
/*
 * Spring Security는 Default 동작으로, 자동으로 Login URL로 넘아감
 * Rest api로 구현하기 위해서는 리다이렉트가 불필요하고 특정 api에 인증없이 request수신시 401을 리턴하도록 구현이 필요함
 */
@Component
public class HttpAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
		
	}
	
}
