package io.github.brfernandes.userservice.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserDto userDto) {  
        User newUser = userService.createUser(userDto);
        UserDto userKafka = modelMapper.map(newUser, UserDto.class);

        kafkaTemplate.send("new-user-topic", userKafka);
        
        return ResponseEntity.ok(newUser);
    }
}


