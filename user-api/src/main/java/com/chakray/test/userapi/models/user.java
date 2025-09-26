package com.chakray.test.userapi.models;

import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
public class user {
    private UUID id;
    private String emailAddress;
    private String name;
    private String phone;
    private String password;
    private String taxID;
    private String createdAt;
    private List<address> addresses;
}
