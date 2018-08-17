package com.tsm.template.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.annotations.Type;
import org.springframework.util.Assert;

import javax.persistence.*;

@Entity
@Table(name = "client")
public class Client extends BaseModel {

	public Client() {

	}

	@Id
	@GeneratedValue
	@Getter
	private Integer id;

	public enum ClientStatus {
		ACTIVE, INACTIVE;
	}

	@Getter
	@Column(nullable = false, length = 30)
	private String name;

	@Getter
	@Column(nullable = false, length = 30)
	private String token;

	@Getter
	@Column(nullable = false, length = 30)
	private String email;

	@Getter
	@Column(nullable = false, length = 10)
	@Enumerated(EnumType.STRING)
	private ClientStatus status;

	@Getter
	@Column(nullable = false, length = 30)
	private String emailRecipient;

	@Column(name = "is_admin")
	@Type(type = "org.hibernate.type.NumericBooleanType")
	@Getter
	@Setter
	public Boolean isAdmin = false;


	public void setName(final String name) {
		Assert.hasText(name, "The name must not be null or empty!");
		this.name = name;
	}

	public void setEmail(final String email) {
		Assert.hasText(email, "The email must not be null or empty!");
		this.email = email;
	}

	public void setEmailRecipient(final String emailRecipient) {
		Assert.hasText(emailRecipient, "The emailRecipient must not be null or empty!");
		this.emailRecipient = emailRecipient;
	}

	public void setToken(final String token) {
		Assert.hasText(token, "The token must not be null or empty!");
		this.token = token;
	}

	public void setClientStatus(final ClientStatus status) {
		Assert.notNull(status, "The status must not be null!");
		this.status = status;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		Client other = (Client) obj;
		if (getId() == null || other.getId() == null) {
			return false;
		}
		return getId().equals(other.getId());
	}

	public static class ClientBuilder {

		private final Client client;

		private ClientBuilder(final String name, final String email, final String token, final ClientStatus status,
				final String emailRecipient, final Boolean isAdmin) {
			client = new Client();
			client.setClientStatus(status);
			client.setEmail(email);
			client.setToken(token);
			client.setName(name);
			client.setIsAdmin(isAdmin);
			client.setEmailRecipient(emailRecipient);

		}

		public static ClientBuilder Client(final String name, final String email, final String token,
				final ClientStatus status, final String emailRecipient, final Boolean isAdmin) {
			return new ClientBuilder(name, email, token, status, emailRecipient, isAdmin);
		}

		public Client build() {
			return client;
		}
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getId()).toHashCode();
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
