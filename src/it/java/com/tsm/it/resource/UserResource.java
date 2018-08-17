package com.tsm.it.resource;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tsm.it.controller.BaseTestIT;
import com.tsm.template.util.UserTestBuilder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Objects;

import static com.jayway.restassured.RestAssured.given;

public class UserResource {

	@JsonIgnore
	@Getter
	@Setter
	private Map<String, String> headers;

	public static UserResource build() {
		return new UserResource();
	}

	public UserResource assertFields() {

		if (Objects.isNull(email)) {
			email();
		}
		if (Objects.isNull(password)) {
			password();
		}
		return this;
	}

	private UserResource() {
	}

	@Getter
	private String email;

	@Getter
	private String password;

	@Getter
	@Setter
	private String token;

	public UserResource auth(final String pEmail, final String pass) {
		this.email(pEmail).password(pass);
		return given().contentType("application/json").body(this).when().post(BaseTestIT.AUTH_URL)
				.as(UserResource.class);
	}

	public UserResource headers(final Map<String, String> headers) {
		this.headers = headers;
		return this;
	}

	public UserResource password(final String password) {
		this.password = password;
		return this;
	}

	public UserResource password() {
		return password(UserTestBuilder.getPassword());
	}

	public UserResource email(final String email) {
		this.email = email;
		return this;
	}

	public UserResource email() {
		return email(UserTestBuilder.getValidEmail());
	}

}
