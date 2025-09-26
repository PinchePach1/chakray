package com.chakray.test.userapi.dto;

import java.util.List;

import com.chakray.test.userapi.models.address;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class userDTO {

    @NotNull(message = "Name is required")
    private String name;

    @NotNull(message = "Tax ID is required")
    @Pattern(regexp = "^[A-ZÑ&]{3,4}[0-9]{6}[A-Z0-9]{3}$", message = "Tax ID must be 13 digits")
    private String taxID;

    @NotNull(message = "Email is required")
    @Email(message = "Email format is invalid")
    private String emailAddress;

    @NotNull(message = "Phone is required")
    @Pattern(regexp = "^\\+[1-9]{1,3}[0-9]{10}$", message = "Phone format is invalid")
    private String phone;

    @NotNull(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    private List<address> addresses;

}

