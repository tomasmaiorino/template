package com.tsm.template.dto;

import lombok.Data;

@Data
public class AuthTokenDTO {

	public AuthTokenDTO(final String token) {
		this.token = token;
	}

	private String token;


}
