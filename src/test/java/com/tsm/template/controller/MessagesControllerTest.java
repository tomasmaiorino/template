package com.tsm.template.controller;

import com.tsm.template.dto.MessageDTO;
import com.tsm.template.exceptions.BadRequestException;
import com.tsm.template.exceptions.MessageException;
import com.tsm.template.exceptions.ResourceNotFoundException;
import com.tsm.template.mappers.MessageMapper;
import com.tsm.template.model.Client;
import com.tsm.template.model.Message;
import com.tsm.template.model.Message.MessageStatus;
import com.tsm.template.service.ClientService;
import com.tsm.template.service.MessageService;
import com.tsm.template.util.ClientTestBuilder;
import com.tsm.template.util.MessageTestBuilder;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@FixMethodOrder(MethodSorters.JVM)
public class MessagesControllerTest {

    private static final String CLIENT_TOKEN = ClientTestBuilder.CLIENT_TOKEN;

    private static final String VALID_HEADER_HOST = "http://localhost";

    private static final Long MESSAGE_ID = null;

    @Mock
    private MessageService service;

    @Mock
    private ClientService clientService;

    @InjectMocks
    private MessagesController controller;

    @Mock
    private Validator validator;

    @Mock
    private MockHttpServletRequest request;

    @Mock
    private MessageMapper messageMapper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(request.getHeader("Referer")).thenReturn(VALID_HEADER_HOST);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    public void save_InvalidMessageDTOGiven_ShouldThrowException() {
        // Set up
        MessageDTO dto = MessageTestBuilder.buildResoure();

        // Expectations
        when(validator.validate(dto, Default.class)).thenThrow(new ValidationException());

        // Do test
        try {
            controller.save(CLIENT_TOKEN, dto, request);
            fail();
        } catch (ValidationException e) {
        }

        // Assertions
        verify(validator).validate(dto, Default.class);
        verifyZeroInteractions(service, clientService, messageMapper);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void save_InvalidClientGiven_ShouldThrowException() {
        // Set up
        MessageDTO dto = MessageTestBuilder.buildResoure();

        // Expectations
        when(validator.validate(dto, Default.class)).thenReturn(Collections.emptySet());
        when(clientService.findByToken(CLIENT_TOKEN)).thenThrow(ResourceNotFoundException.class);

        // Do test
        try {
            controller.save(CLIENT_TOKEN, dto, request);
            fail();
        } catch (ResourceNotFoundException e) {
        }

        // Assertions
        verify(validator).validate(dto, Default.class);
        verify(clientService).findByToken(CLIENT_TOKEN);
        verifyZeroInteractions(service, messageMapper);
    }


    @Test
    public void save_ValidMessageDTOGiven_ShouldSaveMessage() {
        // Set up
        MessageDTO dto = MessageTestBuilder.buildResoure();
        Message message = MessageTestBuilder.buildModel();
        Client client = ClientTestBuilder.buildModel();

        // Expectations
        when(validator.validate(dto, Default.class)).thenReturn(Collections.emptySet());
        when(messageMapper.toModel(dto, client)).thenReturn(message);
        when(clientService.findByToken(CLIENT_TOKEN)).thenReturn(client);
        when(service.save(message)).thenReturn(message);
        when(messageMapper.toDTO(message)).thenReturn(dto);

        // Do test
        MessageDTO result = controller.save(CLIENT_TOKEN, dto, request);

        // Assertions
        verify(validator).validate(dto, Default.class);
        verify(service).save(message);
        verify(messageMapper).toModel(dto, client);
        verify(service).save(message);
        verify(messageMapper).toDTO(message);

        assertNotNull(result);
        assertThat(result,
                allOf(hasProperty("id", notNullValue()), hasProperty("message", is(dto.getMessage())),
                        hasProperty("subject", is(dto.getSubject())),
                        hasProperty("senderName", is(dto.getSenderName())),
                        hasProperty("senderEmail", is(dto.getSenderEmail())),
                        hasProperty("status", is(MessageStatus.CREATED.name()))));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void findById_InvalidClientGiven_ShouldThrowException() {
        // Expectations
        when(clientService.findByToken(CLIENT_TOKEN)).thenThrow(ResourceNotFoundException.class);

        // Do test
        try {
            controller.findById(CLIENT_TOKEN, MESSAGE_ID, request);
            fail();
        } catch (ResourceNotFoundException e) {
        }

        // Assertions
        verify(clientService).findByToken(CLIENT_TOKEN);
        verifyZeroInteractions(service, messageMapper);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void findById_NotFoundMessageGiven_ShouldThrowException() {
        // Set up
        Client client = ClientTestBuilder.buildModel();

        // Expectations
        when(clientService.findByToken(CLIENT_TOKEN)).thenReturn(client);
        when(service.findByIdAndClient(MESSAGE_ID, client)).thenThrow(ResourceNotFoundException.class);

        // Do test
        try {
            controller.findById(CLIENT_TOKEN, MESSAGE_ID, request);
            fail();
        } catch (ResourceNotFoundException e) {
        }

        // Assertions
        verify(clientService).findByToken(CLIENT_TOKEN);
        verify(service).findByIdAndClient(MESSAGE_ID, client);
        verifyZeroInteractions(messageMapper);
    }

    @Test
    public void findById_ValidMessageDTOGiven_ShouldSaveMessage() {
        // Set up
        MessageDTO dto = MessageTestBuilder.buildResoure();
        Message message = MessageTestBuilder.buildModel();
        Client client = ClientTestBuilder.buildModel();

        // Expectations
        when(clientService.findByToken(CLIENT_TOKEN)).thenReturn(client);
        when(service.findByIdAndClient(MESSAGE_ID, client)).thenReturn(message);
        when(messageMapper.toDTO(message)).thenReturn(dto);

        // Do test
        MessageDTO result = controller.findById(CLIENT_TOKEN, MESSAGE_ID, request);

        // Assertions
        verify(clientService).findByToken(CLIENT_TOKEN);
        verify(service).findByIdAndClient(MESSAGE_ID, client);
        verify(messageMapper).toDTO(message);

        assertNotNull(result);
        assertThat(result,
                allOf(hasProperty("id", notNullValue()), hasProperty("message", is(dto.getMessage())),
                        hasProperty("subject", is(dto.getSubject())),
                        hasProperty("senderName", is(dto.getSenderName())),
                        hasProperty("senderEmail", is(dto.getSenderEmail())),
                        hasProperty("status", is(MessageStatus.CREATED.name()))));
    }

}
