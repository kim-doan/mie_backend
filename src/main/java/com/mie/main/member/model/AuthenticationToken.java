package com.mie.main.member.model;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AuthenticationToken {
	
	private String username;
	private String type;
	private String token;
	
	public AuthenticationToken(String username, String type, String token) {
		this.username = username;
		this.type = type;
		this.token = token;
	}
}
