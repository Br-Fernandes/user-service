package io.github.brfernandes.userservice.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import io.github.brfernandes.userservice.dtos.UserDto;
import io.github.brfernandes.userservice.models.Confirmation;
import io.github.brfernandes.userservice.models.User;
import io.github.brfernandes.userservice.repositories.ConfirmationRepository;
import io.github.brfernandes.userservice.repositories.UserRepository;
import io.github.brfernandes.userservice.services.UserService;
import io.github.brfernandes.userservice.utils.exceptions.EmailExistsException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ConfirmationRepository confirmationRepository;
    private final ModelMapper modelMapper;
    
    @Override
    public User createUser(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new EmailExistsException(userDto.getEmail());
        }

        User user = modelMapper.map(userDto, User.class);

        Confirmation confirmation = new Confirmation(user);

        User newUser = userRepository.save(user);
        confirmationRepository.save(confirmation);

        return newUser;
    }


    @Override
    public Boolean verifyToken(String token) {
        User user = findByToken(token);
        user.setEnabled(true);
        userRepository.save(user);
        return true;
    }

    @Override
    public User findByToken(String token) {
        Confirmation confirmation = confirmationRepository.findByToken(token);
        return userRepository.findByEmail(confirmation.getUser().getEmail());
    }
}
