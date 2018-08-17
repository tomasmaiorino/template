package com.tsm.template.controller;

import com.tsm.template.dto.ClientDTO;
import com.tsm.template.mappers.IBaseMapper;
import com.tsm.template.model.BaseModel;
import com.tsm.template.model.Client;
import com.tsm.template.service.BaseService;
import com.tsm.template.service.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(value = "/api/v1/clients")
@Slf4j
public class ClientsController extends RestBaseController<ClientDTO, Client, Integer> {

    @Autowired
    private ClientService service;

    @Autowired
    private ModelMapper modelMapper;

    @RequestMapping(method = POST, consumes = JSON_VALUE, produces = JSON_VALUE)
    @ResponseStatus(CREATED)
    @ResponseBody
    public ClientDTO save(@RequestBody final ClientDTO resource, final HttpServletRequest request) {
        return super.save(resource);
    }

    @Override
    protected IBaseMapper getMapper() {
        return new IBaseMapper() {
            @Override
            public BaseModel toModel(final Object dto) {
                return modelMapper.map(dto, Client.class);
            }

            @Override
            public Object toDTO(final BaseModel model) {
                return modelMapper.map(model, ClientDTO.class);
            }

            @Override
            public Set toDTOs(Set models) {
                return null;
            }
        };
    }

    @Override
    public BaseService<Client, Integer> getService() {
        return service;
    }

}
