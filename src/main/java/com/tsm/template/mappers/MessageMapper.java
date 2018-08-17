package com.tsm.template.mappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsm.template.dto.MessageDTO;
import com.tsm.template.model.Client;
import com.tsm.template.model.Message;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class MessageMapper implements IBaseMapper<MessageDTO, Message> {

    @Autowired
    private ModelMapper modelMapper;

    public Message toModel(MessageDTO dto, Client client) {
        return null;
    }

    @Override
    public Message toModel(MessageDTO dto) {
        return modelMapper.map(dto, Message.class);
    }

    @Override
    public MessageDTO toDTO(Message model) {
        return modelMapper.map(model, MessageDTO.class);
    }

    @Override
    public Set<MessageDTO> toDTOs(Set<Message> models) {
        return null;
    }
}
