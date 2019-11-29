/**
 * 
 * 2019.09.24
 * 김도안
 * Exception 리다이렉트 경로
 *  
 * entrypoint - 토큰 정보가 없거나 만료됬을 경우
 * accessdenied - 토큰 권한이 페이지 권한 보다 낮을 경우
 * 
 */

package com.mie.main.exception.controller;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mie.main.member.model.CommonResult;
import com.mie.main.security.exception.CAuthenticationEntryPointException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/exception")
public class ExceptionController {
	
	@GetMapping(value = "/entrypoint")
	public CommonResult entrypointException() {
		throw new CAuthenticationEntryPointException();
	}
	
	@GetMapping(value = "/accessdenied")
	public CommonResult accessdeniedException() {
	    throw new AccessDeniedException("");
	}
}
