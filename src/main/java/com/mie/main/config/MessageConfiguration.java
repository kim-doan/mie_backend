/**
 * 
 * 2019.09.24
 * 김도안
 * Exception 에러 메시지를 담고 있는 .yml 파일 경로 잡아주기
 * lang = un 일경우 에러메시지 영어 kr일 경우 한국어
 * 
 */

package com.mie.main.config;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import net.rakugakibox.util.YamlResourceBundle;

@Configuration
public class MessageConfiguration implements WebMvcConfigurer{
	
	@Bean // 세션에 지역 설정. 
	public LocaleResolver localeResolver() {
		SessionLocaleResolver slr = new SessionLocaleResolver();
		slr.setDefaultLocale(Locale.KOREAN);
		return slr;
	}
	
	@Bean // 지역설정을 변경하는 인터넷 요청시 파라미터에 lang 정보를 저장하면 언어가 변경됨
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
		lci.setParamName("lang");
		return lci;
	}
	
	@Override // 인터셉터를 시스템 레지스트리에 등록
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}
	
	@Bean
	public MessageSource messageSource(
			@Value("${spring.messages.basename}") String basename,
			@Value("${spring.messages.encoding}") String encoding) {
		YamlMessageSource ms = new YamlMessageSource();
		ms.setBasename(basename);
		ms.setDefaultEncoding(encoding);
		ms.setAlwaysUseMessageFormat(true);
		ms.setUseCodeAsDefaultMessage(true);
		ms.setFallbackToSystemLocale(true);
		return ms;
	}
	
	private static class YamlMessageSource extends ResourceBundleMessageSource {
		@Override
		protected ResourceBundle doGetBundle(String basename, Locale locale) throws MissingResourceException {
			return ResourceBundle.getBundle(basename, locale, YamlResourceBundle.Control.INSTANCE);
		}
	}
}
