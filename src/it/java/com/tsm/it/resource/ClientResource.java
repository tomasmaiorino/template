package com.tsm.it.resource;

import com.tsm.template.model.Client;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static com.jayway.restassured.RestAssured.given;
import static org.apache.commons.lang3.RandomStringUtils.random;

public class ClientResource extends BaseItResource {

	public static ClientResource build() {
		return new ClientResource();
	}

	public ClientResource assertFields() {
		if (Objects.isNull(name)) {
			name();
		}
		if (Objects.isNull(token)) {
			token();
		}
		if (Objects.isNull(email)) {
			email();
		}
		if (Objects.isNull(status)) {
			status();
		}
		if (Objects.isNull(emailRecipient)) {
			emailRecipient();
		}
		return this;
	}

	public ClientResource create() {
		assertFields();
		return given().headers(getHeaders()).contentType("application/json").body(this).when().post("/api/v1/clients")
				.as(ClientResource.class);
	}

	private ClientResource() {
	}

	@Getter
	@Setter
	private Integer id;

	@Getter
	private String name;

	@Getter
	private String email;

	@Getter
	private String token;

	@Getter
	private String status;

	@Getter
	private Set<String> hosts;

	@Getter
	private String emailRecipient;

	@Getter
	public Boolean isAdmin = false;

	@Getter
	private Map<String, String> attributes;

	public ClientResource emailRecipient(final String emailRecipient) {
		this.emailRecipient = emailRecipient;
		return this;
	}

	public ClientResource headers(Map<String, String> headers) {
		this.headers = headers;
		return this;
	}

	public ClientResource emailRecipient() {
		return emailRecipient(random(20, true, true) + "@" + random(2, true, false) + ".com");
	}

	public ClientResource email(final String email) {
		this.email = email;
		return this;
	}

	public ClientResource email() {
		return email(random(20, true, true) + "@" + random(2, true, false) + ".com");
	}

	public ClientResource isAdmin(final Boolean isAdmin) {
		this.isAdmin = isAdmin;
		return this;
	}

	public ClientResource token(final String token) {
		this.token = token;
		return this;
	}

	public ClientResource token() {
		return token(random(30, true, true));
	}

	public ClientResource status() {
		return status(Client.ClientStatus.values()[Integer.parseInt(RandomStringUtils.randomNumeric(Client.ClientStatus.values().length - 1))].name());
	}

	public ClientResource status(final String status) {
		this.status = status;
		return this;
	}

	public ClientResource name() {
		return name(random(30, true, true));
	}

	public ClientResource name(final String name) {
		this.name = name;
		return this;
	}
}
