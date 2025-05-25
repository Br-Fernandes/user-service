package io.github.brfernandes.userservice.services.impl;

import io.github.brfernandes.userservice.dtos.UserDto;
import io.github.brfernandes.userservice.models.User;
import io.github.brfernandes.userservice.repositories.UserRepository;
import io.github.brfernandes.userservice.services.UserService;
import io.github.brfernandes.userservice.utils.exceptions.EmailExistsException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public User createUser(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())){
            throw new EmailExistsException(userDto.getEmail());
        }

        User newUser = modelMapper.map(userDto, User.class);

        return userRepository.save(newUser);
    }
}
