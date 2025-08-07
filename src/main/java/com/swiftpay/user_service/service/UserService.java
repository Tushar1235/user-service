package com.swiftpay.user_service.service;

import com.swiftpay.user_service.dto.UserDto;
import com.swiftpay.user_service.exception.UserAlreadyExistsException;
import com.swiftpay.user_service.exception.UserNotFoundException;
import com.swiftpay.user_service.model.User;
import com.swiftpay.user_service.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Transactional
    public User registerUser(UserDto userDto) {
        logger.info("Registering user with email: {}",userDto.getEmail());
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        if(userRepository.existsByEmail(userDto.getEmail())) {
            logger.error("Attempting to register duplicate email: {}",userDto.getEmail());
            throw new UserAlreadyExistsException("User already exits");
        }
        User user =modelMapper.map(userDto, User.class);
        user.setRoles(List.of("ROLE_USER"));
        logger.info("User registered successfully with id: {}",user.getEmail());
        return userRepository.save(user);
    }

    public UserDto getUser(String email) {
        logger.info("Fetching user by email: {}", email);
        User user = userRepository.findByEmail(email).orElseThrow(()->{
            logger.error("User not found for email: {}",email);
            return new UserNotFoundException("User not found");
        });

        return modelMapper.map(user, UserDto.class);
    }
    public List<UserDto> getAllUsers() {
        logger.info("Fetching all user");
        List<UserDto> userDtoList = userRepository.findAll().stream()
                .map(user-> modelMapper.map(user, UserDto.class))
                .toList();

        return userDtoList;
    }


    @PostConstruct
    public void registerAdmin() {
        if (!userRepository.existsByEmail("user123@gmail.com")) {
            User admin = new User();
            admin.setEmail("user123@gmail.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRoles(List.of("ROLE_ADMIN", "ROLE_USER"));
            userRepository.save(admin);
        }
    }
}
