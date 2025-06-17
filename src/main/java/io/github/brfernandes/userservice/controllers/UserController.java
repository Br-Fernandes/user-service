package io.github.brfernandes.userservice.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.brfernandes.userservice.dtos.UserAuthenticationDto;
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
    private final KafkaTemplate<String, UserAuthenticationDto> authenticationKafkaTemplate;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserDto userDto) {  
        User newUser = userService.createUser(userDto);
        UserDto userKafka = modelMapper.map(newUser, UserDto.class);

        kafkaTemplate.send("new-user-topic", userKafka);
        return ResponseEntity.ok(newUser);
    }       

    @GetMapping("/activate")
    public ResponseEntity<String> activateUser(@RequestParam String token) {
        System.out.println("to aqui");
        User user = userService.findByToken(token);
        
        if (userService.verifyToken(token)){
            authenticationKafkaTemplate.send("new-jwt-topic", new UserAuthenticationDto(user.getEmail(), user.getPassword(), user.isEnabled()));
            return ResponseEntity.ok("User Activated Successfully");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Token");
    }
}


