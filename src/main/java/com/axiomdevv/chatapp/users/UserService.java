package com.axiomdevv.chatapp.users;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {


    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User createUser(String username , String email , String passwordHash){
        if(userRepository.existsByUsername(username) ){
            throw new UsernameTakenException(username);
        }
        if (userRepository.existsByEmail(email)){
            throw new EmailTakenException(email);
        }

        return userRepository.saveAndFlush(new User(username,email,passwordHash));
    }

    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }




}
