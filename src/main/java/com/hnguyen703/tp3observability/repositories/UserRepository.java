package com.hnguyen703.tp3observability.repositories;

import com.hnguyen703.tp3observability.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
}
