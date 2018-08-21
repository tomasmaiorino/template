package com.tsm.template.mappers;

import com.tsm.template.dto.MessageDTO;
import com.tsm.template.model.Client;
import com.tsm.template.model.Message;
import io.jsonwebtoken.lang.Assert;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class MessageMapper implements IBaseMapper<MessageDTO, Message> {

    @Autowired
    private ModelMapper modelMapper;

    public Message toModel(MessageDTO dto, Client client) {
        Assert.notNull(dto, "The dto must not be null.");
        Assert.notNull(client, "The client must not be null.");

        Message message = modelMapper.map(dto, Message.class);
        message.setClient(client);
        return message;
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
