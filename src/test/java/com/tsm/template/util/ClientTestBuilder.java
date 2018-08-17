package com.tsm.template.util;

import com.tsm.template.dto.ClientDTO;
import com.tsm.template.model.Client;

import static org.apache.commons.lang3.RandomStringUtils.random;

public class ClientTestBuilder {

    public static final String LARGE_NAME = random(31, true, true);
    public static final String SMALL_NAME = random(1, true, true);

    public static final String LARGE_TOKEN = random(51, true, true);
    public static final String SMALL_TOKEN = random(1, true, true);
    public static final String RESOURCE_INVALID_EMAIL = random(31, true, true);
    public static final String INVALID_STATUS = "INVD";

    public static final String[] HOSTS = new String[]{"http://mysite.com.br"};
    public static final String CLIENT_NAME = random(30, true, true);
    public static final String CLIENT_TOKEN = random(30, true, true);
    public static final String CLIENT_EMAIL = "email@site.com";
    public static final String CLIENT_EMAIL_RECEIPIENT = "email@site.com";

    private static final Client.ClientStatus STATUS = Client.ClientStatus.ACTIVE;


    public static Client buildModel() {
        return buildModel(CLIENT_NAME, CLIENT_TOKEN, STATUS, CLIENT_EMAIL, CLIENT_EMAIL_RECEIPIENT);
    }

    public static Client buildModel(final String name, final String token,
                                    final Client.ClientStatus status, final String email, final String emailRecipient) {
        Client client = Client.ClientBuilder.Client(name, email, token, status, emailRecipient, false).build();
        return client;
    }

    public static ClientDTO buildDTO(final String name, final String token, final String email,
                                     final String status, final String emailRecipient) {
        ClientDTO resource = new ClientDTO();
        resource.setName(name);
        resource.setEmail(email);
        resource.setToken(token);
        resource.setStatus(status);
        resource.setEmailRecipient(emailRecipient);
        return resource;
    }

    public static ClientDTO buildDTO() {
        return buildDTO(CLIENT_NAME, CLIENT_TOKEN, CLIENT_EMAIL, STATUS.toString(),
                CLIENT_EMAIL_RECEIPIENT);
    }
}
