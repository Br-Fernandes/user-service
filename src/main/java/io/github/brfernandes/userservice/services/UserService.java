package io.github.brfernandes.userservice.services;

import io.github.brfernandes.userservice.dtos.UserDto;
import io.github.brfernandes.userservice.models.User;

public interface UserService {

    User createUser(UserDto userDto);

    Boolean verifyToken(String token);

    User findByToken(String token);
}
