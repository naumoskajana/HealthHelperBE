package com.example.healthhelper.service;

import com.example.healthhelper.dto.UserLoginDTO;
import com.example.healthhelper.dto.UserRegistrationDTO;
import com.example.healthhelper.entity.Disease;
import com.example.healthhelper.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    void register(UserRegistrationDTO userRegistrationDTO);
    User login(UserLoginDTO userLoginDTO);
    User findByEmail(String email);
    void addDisease(User user, Disease disease);

}
