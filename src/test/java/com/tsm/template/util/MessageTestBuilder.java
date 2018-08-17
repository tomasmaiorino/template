package com.tsm.template.util;

import com.tsm.template.dto.MessageDTO;
import com.tsm.template.model.Client;
import com.tsm.template.model.Message;

import static org.apache.commons.lang3.RandomStringUtils.random;

public class MessageTestBuilder {

    public static final String LARGE_MESSAGE = random(Message.MESSAGE_MAX_LENGTH + 1, true, true);
    public static final String SMALL_MESSAGE = random(1, true, true);
    public static final String LARGE_SUBJECT = random(31, true, true);
    public static final String SMALL_SUBJECT = random(1, true, true);
    public static final String LARGE_SENDER_NAME = random(31, true, true);
    public static final String SMALL_SENDER_NAME = random(1, true, true);
    public static final String LARGE_SENDER_EMAIL = random(31, true, true);
    public static final String RESOURCE_INVALID_EMAIL = random(31, true, true);
    public static final String INVALID_STATUS = "INVD";
    public static final String MESSAGE = random(30, true, true);
    public static final String SUBJECT = random(30, true, true);
    public static final String SENDER_EMAIL = "email@site.com";
    public static final String SENDER_NAME = "email@site.com";
    public static final Client CLIENT = buildClient();
    public static final Message.MessageStatus STATUS = Message.MessageStatus.CREATED;
    public static final Long MESSAGE_ID = 10l;

    private static Client buildClient() {
        return ClientTestBuilder.buildModel();
    }

    public static Message buildModel() {
        return buildModel(MESSAGE, SENDER_EMAIL, SENDER_NAME, SUBJECT, CLIENT, STATUS);
    }

    public static Message buildModel(final String pMessage, final String senderEmail, final String senderName,
        final String subject, final Client client, final Message.MessageStatus status) {
        Message message = new Message();
        message.setClient(client);
        message.setMessage(pMessage);
        message.setSenderEmail(senderEmail);
        message.setSenderName(senderName);
        message.setSubject(subject);
        message.setStatus(status);

        return message;
    }

    public static MessageDTO buildResoure(final Long id, final String pMessage, final String senderEmail,
                                          final String senderName, final String subject, final String status) {
        MessageDTO resource = new MessageDTO();
        resource.setMessage(pMessage);
        resource.setSenderEmail(senderEmail);
        resource.setSenderName(senderName);
        resource.setStatus(status);
        resource.setSubject(subject);
        resource.setId(id);
        return resource;
    }

    public static MessageDTO buildResoure() {
        return buildResoure(MESSAGE_ID, MESSAGE, SENDER_EMAIL, SENDER_NAME, SUBJECT, STATUS.name());
    }
}
