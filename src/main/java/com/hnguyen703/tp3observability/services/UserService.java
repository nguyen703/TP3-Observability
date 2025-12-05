package com.hnguyen703.tp3observability.services;

import com.hnguyen703.tp3observability.models.User;
import com.hnguyen703.tp3observability.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }
}
