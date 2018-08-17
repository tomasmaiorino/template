package com.tsm.template.controller;

import com.tsm.template.dto.ClientDTO;
import com.tsm.template.exceptions.BadRequestException;
import com.tsm.template.mappers.IBaseMapper;
import com.tsm.template.model.Client;
import com.tsm.template.service.ClientService;
import com.tsm.template.util.ClientTestBuilder;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@FixMethodOrder(MethodSorters.JVM)
public class ClientsControllerTest {


    @Mock
    private ClientService service;

    @InjectMocks
    private ClientsController controller;

    @Mock
    private Validator validator;

    @Mock
    private MockHttpServletRequest request;

    private ModelMapper modelMapper = new ModelMapper();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        ReflectionTestUtils.setField(controller, "modelMapper", modelMapper);
    }

    @Test
    public void save_InvalidClientDTOGiven_ShouldThrowException() {
        // Set up
        ClientDTO dto = ClientTestBuilder.buildDTO();

        // Expectations
        when(validator.validate(dto, Default.class)).thenThrow(new ValidationException());

        // Do test
        try {
            controller.save(dto, request);
            fail();
        } catch (ValidationException e) {
        }

        // Assertions
        verify(validator).validate(dto, Default.class);
        verifyZeroInteractions(service);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void save_DuplicatedClientDTOGiven_ShouldSaveClient() {
        // Set up
        ClientDTO dto = ClientTestBuilder.buildDTO();

        // Expectations
        when(validator.validate(dto, Default.class)).thenReturn(Collections.emptySet());
        when(service.save(any(Client.class))).thenThrow(BadRequestException.class);

        // Do test
        try {
            controller.save(dto, request);
            fail();
        } catch (BadRequestException e) {
        }

        // Assertions
        verify(validator).validate(dto, Default.class);
        verify(service).save(any(Client.class));
    }

    @Test
    public void save_ValidClientDTOGiven_ShouldSaveClient() {
        // Set up
        ClientDTO dto = ClientTestBuilder.buildDTO();
        Client client = ClientTestBuilder.buildModel();

        // Expectations
        when(validator.validate(dto, Default.class)).thenReturn(Collections.emptySet());
        when(service.save(any(Client.class))).thenReturn(client);

        // Do test
        ClientDTO result = controller.save(dto, request);

        // Assertions
        verify(validator).validate(dto, Default.class);
        verify(service).save(any(Client.class));

        assertNotNull(result);
    }
}
