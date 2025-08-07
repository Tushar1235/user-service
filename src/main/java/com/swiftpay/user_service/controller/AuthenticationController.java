package com.swiftpay.user_service.controller;

import com.swiftpay.user_service.configuration.Util.JWTUtil;
import com.swiftpay.user_service.dto.AuthRequest;
import com.swiftpay.user_service.dto.AuthResponse;
import com.swiftpay.user_service.dto.UserDto;
import com.swiftpay.user_service.model.User;
import com.swiftpay.user_service.service.UserService;
import com.swiftpay.user_service.service.security.CustomUserDetailsService;
import com.swiftpay.user_service.service.security.CustomeUserDetails;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private JWTUtil util;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);


    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody @Valid UserDto userDto) {
        logger.info("Received POST /users/register/");
        User user = userService.registerUser(userDto);
        UserDto userDto1 = modelMapper.map(user, UserDto.class);
        return new ResponseEntity<>(userDto1, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> handleLogin(@RequestBody AuthRequest request) {

        Authentication authentication = manager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(request.getUsername());
        String token = util.generateToken(userDetails);
        String accountToken = util.generateInternalToken();
        logger.info("account-service toekn: {}",accountToken);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new AuthResponse(token));
    }

    @GetMapping("/getRole/{username}")
    public ResponseEntity<List<String>> register(@PathVariable String username) {
        UserDto user = userService.getUser(username);
        return new ResponseEntity<>(user.getRoles(), HttpStatus.CREATED);
    }
}
