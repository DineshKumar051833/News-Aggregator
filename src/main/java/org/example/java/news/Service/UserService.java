package org.example.java.news.Service;

import org.example.java.news.Model.User;
import org.example.java.news.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User registerUser(User user){
        return userRepository.save(user);
    }

    public Optional<User> getUserByEmailAndPassword(String email, String password){
        return userRepository.getUserByEmailAndPassword(email,password);
    }

    public Optional<User> getUserByEmail(String email){
        return userRepository.getUserByEmail(email);
    }

}
