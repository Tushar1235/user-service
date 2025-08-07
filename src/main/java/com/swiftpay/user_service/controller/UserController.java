package com.swiftpay.user_service.controller;

import com.swiftpay.user_service.dto.UserDto;
import com.swiftpay.user_service.model.User;
import com.swiftpay.user_service.service.UserService;
import com.swiftpay.user_service.service.security.CustomUserDetailsService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);


    @GetMapping("/profile/{username}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String username) {

        logger.info("Received GET /users/profile/{}", username);
        UserDetails userDetails =  customUserDetailsService.loadUserByUsername(username);
        UserDto user = userService.getUser(userDetails.getUsername());
        logger.info("Returning user: {}", user);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/admin/allusers")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        logger.info("Received GET /admin/allusers/");
        List<UserDto> user = userService.getAllUsers();
        return ResponseEntity.ok(user);
    }

}
