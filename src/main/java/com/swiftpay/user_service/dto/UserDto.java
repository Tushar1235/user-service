package com.swiftpay.user_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserDto {
    @NotBlank(message = "Name required")
    private String name;

    @Email(message = "Enter an valid email")
    @NotBlank(message = "Email required")
    private String email;

    @Size(min = 6, message = "Password should be atleast 6 character")
    @JsonProperty(access =  JsonProperty.Access.WRITE_ONLY)
    private String password;

    private LocalDateTime createdAt;

    private List<String> roles;
}
