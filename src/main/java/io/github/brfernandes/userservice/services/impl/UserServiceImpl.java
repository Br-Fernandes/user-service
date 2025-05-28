package io.github.brfernandes.userservice.services.impl;

import io.github.brfernandes.userservice.dtos.UserDto;
import io.github.brfernandes.userservice.models.Confirmation;
import io.github.brfernandes.userservice.models.User;
import io.github.brfernandes.userservice.repositories.ConfirmationRepository;
import io.github.brfernandes.userservice.repositories.UserRepository;
import io.github.brfernandes.userservice.services.UserService;
import io.github.brfernandes.userservice.utils.exceptions.EmailExistsException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ConfirmationRepository confirmationRepository;
    private final ModelMapper modelMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public User createUser(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())){
            throw new EmailExistsException(userDto.getEmail());
        }

        User newUser = modelMapper.map(userDto, User.class);
        kafkaTemplate.send("create-user-topic", userDto.getEmail());
        return userRepository.save(newUser);
    }

    @Override
    public Boolean verifyToken(String token) {
        Confirmation confirmation = confirmationRepository.findByToken(token);
        User user = userRepository.findByEmail(confirmation.getUser().getEmail());
        user.setEnabled(true);
        userRepository.save(user);
        //confirmationRepository.delete(confirmation);
        return true;
    }
}
