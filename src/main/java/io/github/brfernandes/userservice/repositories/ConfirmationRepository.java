package io.github.brfernandes.userservice.repositories;

import io.github.brfernandes.userservice.models.Confirmation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmationRepository extends MongoRepository<Confirmation, String> {

    Confirmation findByToken(String token);
}
