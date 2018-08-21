package com.tsm.template.controller;


import com.tsm.template.dto.MessageDTO;
import com.tsm.template.exceptions.BadRequestException;
import com.tsm.template.exceptions.ResourceNotFoundException;
import com.tsm.template.mappers.IBaseMapper;
import com.tsm.template.mappers.MessageMapper;
import com.tsm.template.model.Client;
import com.tsm.template.model.Message;
import com.tsm.template.service.BaseService;
import com.tsm.template.service.ClientService;
import com.tsm.template.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.groups.Default;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static com.tsm.template.util.ErrorCodes.CLIENT_NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(value = "/api/v1/messages/{clientToken}")
@Slf4j
public class MessagesController extends RestBaseController<MessageDTO, Message, Long> {

    @Autowired
    private MessageService service;

    @Autowired
    private ClientService clientService;

    @Autowired
    private MessageMapper mapper;

    private List<String> searchParamsAllowed = Arrays.asList("subject", "status");

    @RequestMapping(method = POST, consumes = JSON_VALUE, produces = JSON_VALUE, headers = {
            "content-type=application/json"})
    @ResponseStatus(OK)
    public MessageDTO save(@PathVariable String clientToken, @RequestBody final MessageDTO resource,
                           HttpServletRequest request) {
        log.debug("Received a request to create a message [{}] for the client token [{}].", resource, clientToken);

        validate(resource, Default.class);

        Client client = clientService.findByToken(clientToken);

        resource.setStatus(Message.MessageStatus.CREATED.name());

        Message message = mapper.toModel(resource, client);

        message = service.save(message);

        MessageDTO result = getMapper().toDTO(message);

        log.debug("Returning resource [{}].", result);

        return result;
    }

    @RequestMapping(method = GET, path = "/{id}", consumes = JSON_VALUE, produces = JSON_VALUE)
    @ResponseStatus(OK)
    public MessageDTO findById(@PathVariable String clientToken, @PathVariable Long id,
                               HttpServletRequest request) {
        log.debug("Received a request to search for a message by id [{}] and client token [{}].", id, clientToken);

        Client client = recoverClient(clientToken);

        Message message = service.findByIdAndClient(id, client);

        MessageDTO result = getMapper().toDTO(message);

        log.debug("Returning resource [{}].", result);

        return result;
    }

    private Client recoverClient(final String token) {
        Client client = null;
        try {
            client = clientService.findByToken(token);
        } catch (ResourceNotFoundException re) {
            throw new BadRequestException(CLIENT_NOT_FOUND);
        }
        return client;
    }

    @RequestMapping(method = GET, produces = JSON_VALUE, path = "/find")
    @ResponseStatus(OK)
    public Set<MessageDTO> findAll(@RequestParam(value = "search", required = false) String search) {
        return findAllSearch(search, searchParamsAllowed, (p) -> service.search(p), Message.MessageStatus.class);
    }

    @Override
    public BaseService<Message, Long> getService() {
        return service;
    }

    @Override
    public IBaseMapper<MessageDTO, Message> getMapper() {
        return mapper;
    }

}
