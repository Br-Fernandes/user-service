package io.github.brfernandes.userservice.utils.exceptions;

public class EmailExistsException extends RuntimeException {

    public EmailExistsException(String email) {
        super("Email: " + email + " already exists!");
    }
}
