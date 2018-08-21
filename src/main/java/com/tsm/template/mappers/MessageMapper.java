package com.tsm.template.mappers;

import com.tsm.template.dto.MessageDTO;
import com.tsm.template.model.Client;
import com.tsm.template.model.Message;
import io.jsonwebtoken.lang.Assert;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;

@Component
public class MessageMapper implements IBaseMapper<MessageDTO, Message> {


    private ModelMapper modelMapper = createModelMapper();

    private ModelMapper createModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addConverter((Converter<Message.MessageStatus, String>) mappingContext -> Objects.nonNull(mappingContext.getSource()) ? mappingContext.getSource().name() : "");
        modelMapper.addConverter((Converter<String, Message.MessageStatus>) mappingContext -> StringUtils.isNotBlank(mappingContext.getSource()) ? Message.MessageStatus.valueOf(mappingContext.getSource()) : null);
        return modelMapper;
    }

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
