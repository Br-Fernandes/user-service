package io.github.brfernandes.userservice.utils.serializers;

import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.brfernandes.userservice.dtos.UserAuthenticationDto;

public class UserAuthenticationSerializer implements Serializer<UserAuthenticationDto>{

    @Override
    public byte[] serialize(String topic, UserAuthenticationDto user) {
        try {
            return new ObjectMapper().writeValueAsBytes(user);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
