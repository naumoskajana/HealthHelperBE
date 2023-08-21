package com.example.healthhelper.service.impl;

import com.example.healthhelper.dto.UserLoginDTO;
import com.example.healthhelper.dto.UserRegistrationDTO;
import com.example.healthhelper.entity.Disease;
import com.example.healthhelper.entity.User;
import com.example.healthhelper.enumeration.UserType;
import com.example.healthhelper.exceptions.UserAlreadyExistsException;
import com.example.healthhelper.repository.UserRepository;
import com.example.healthhelper.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void register(UserRegistrationDTO userRegistrationDTO) {
        User existingUser = userRepository.findByEmail(userRegistrationDTO.getEmail());

        if (existingUser != null){
            throw new UserAlreadyExistsException(userRegistrationDTO.getEmail());
        }

        User newUser = new User();
        newUser.setFullName(userRegistrationDTO.getFullName());
        newUser.setEmail(userRegistrationDTO.getEmail());
        newUser.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));
        newUser.setUserType(UserType.USER);
        newUser.setDiseases(new ArrayList<>());
        userRepository.save(newUser);

    }

    @Override
    public User login(UserLoginDTO userLoginDTO) {
        User user = findByEmail(userLoginDTO.getEmail());
        return userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void addDisease(User user, Disease disease) {
        boolean hasDisease = user.getDiseases().stream().anyMatch(d -> d.getName().equals(disease.getName()));
        if (!hasDisease) {
            user.getDiseases().add(disease);
        }
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email);
    }
}
