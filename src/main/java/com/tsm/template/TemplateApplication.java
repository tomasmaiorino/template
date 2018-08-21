package com.tsm.template;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsm.template.dto.MessageDTO;
import com.tsm.template.model.Message;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Objects;

@SpringBootApplication
public class TemplateApplication {

    @Bean
    public ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<MessageDTO, Message>() {
            @Override
            protected void configure() {
                map().setStatus(Objects.nonNull(source) && StringUtils.isNotBlank(source.getStatus()) ? Message.MessageStatus.valueOf(source.getStatus()) : Message.MessageStatus.CREATED);
            }
        });
        modelMapper.addMappings(new PropertyMap<Message, MessageDTO>() {
            @Override
            protected void configure() {
                map().setStatus(Objects.nonNull(source) && Objects.nonNull(source.getStatus()) ? source.getStatus().name() : "");
            }
        });
        return modelMapper;
    }

    @Bean
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }

    public static void main(String[] args) {
        SpringApplication.run(TemplateApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
