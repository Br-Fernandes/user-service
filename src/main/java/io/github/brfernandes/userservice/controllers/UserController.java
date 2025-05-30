package io.github.brfernandes.userservice.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.brfernandes.userservice.dtos.UserDto;
import io.github.brfernandes.userservice.models.User;
import io.github.brfernandes.userservice.services.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final KafkaTemplate<String, UserDto> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserDto userDto) {
        String userJson;
        try {
            userJson = objectMapper.writeValueAsString(userDto);
            kafkaTemplate.send("new-user-topic", userDto);
        } catch (JsonProcessingException ex) {
        }
        
        return ResponseEntity.ok(userService.createUser(userDto));
    }
}


