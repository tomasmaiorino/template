package com.tsm.template.dto;

import lombok.Getter;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Map;

import static com.tsm.template.util.ErrorCodes.*;

public class ClientDTO implements BaseDTO {

    @Getter
    private Integer id;

    @Getter
    @NotNull(message = REQUIRED_NAME)
    @Size(min = 2, max = 30, message = INVALID_NAME_SIZE)
    private String name;

    @Getter
    @NotEmpty(message = REQUIRED_EMAIL)
    @Email(message = INVALID_EMAIL)
    private String email;

    @Getter
    @NotNull(message = REQUIRED_TOKEN)
    @Size(min = 2, max = 50, message = INVALID_TOKEN_SIZE)
    private String token;

    @Getter
    @Pattern(regexp = "\\b(ACTIVE|INACTIVE)\\b", message = INVALID_STATUS)
    private String status;

    @Getter
    @NotEmpty(message = REQUIRED_EMAIL_RECIPIENT)
    @Email(message = INVALID_EMAIL)
    private String emailRecipient;

    @Getter
    private Map<String, String> attributes;

    @Getter
    private Boolean isAdmin = false;

    public void setId(final Integer id) {
        this.id = id;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public void setToken(final String token) {
        this.token = token;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public void setEmailRecipient(final String emailRecipient) {
        this.emailRecipient = emailRecipient;
    }

    public void setAttributes(final Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public void setIsAdmin(final Boolean isAdmin) {
        this.isAdmin = isAdmin;
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
        ClientDTO other = (ClientDTO) obj;
        if (getId() == null || other.getId() == null) {
            return false;
        }
        return getId().equals(other.getId());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
