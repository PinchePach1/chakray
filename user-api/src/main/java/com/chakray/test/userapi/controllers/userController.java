package com.chakray.test.userapi.controllers;

import com.chakray.test.userapi.models.user;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.Map;
import com.chakray.test.userapi.utils.cryptoUtils;
import jakarta.validation.Valid;
import com.chakray.test.userapi.dto.userDTO;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;

@RestController
@RequestMapping("/users")
public class userController {
    
    private List<user> users = new ArrayList<>();

    // @GetMapping //listar todos os usuarios
    // public List<user> getUsers() {
    //     return users;
    // }

    @PostMapping //crear usuario
    public ResponseEntity<?> createUser(@Valid @RequestBody userDTO userDTO) {
        boolean exists = users.stream()
                .anyMatch(u -> u.getTaxID().equalsIgnoreCase(userDTO.getTaxID()));
        if (exists) {
            return ResponseEntity.badRequest().body("Tax ID must be unique");
        }
        user newUser = new user();
        newUser.setId(UUID.randomUUID());
        newUser.setName(userDTO.getName());
        newUser.setTaxID(userDTO.getTaxID());
        newUser.setEmailAddress(userDTO.getEmailAddress());
        newUser.setPhone(userDTO.getPhone());
        newUser.setAddresses(userDTO.getAddresses());
        ZonedDateTime nowInMadagascar = ZonedDateTime.now(ZoneId.of("Indian/Antananarivo"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        newUser.setCreatedAt(nowInMadagascar.format(formatter));
        String encrypted = cryptoUtils.encrypt(userDTO.getPassword());
        newUser.setPassword(encrypted);
        users.add(newUser);
        user safeResponse = new user();
        safeResponse.setId(newUser.getId());
        safeResponse.setEmailAddress(newUser.getEmailAddress());
        safeResponse.setName(newUser.getName());
        safeResponse.setPhone(newUser.getPhone());
        safeResponse.setTaxID(newUser.getTaxID());
        safeResponse.setCreatedAt(newUser.getCreatedAt());
        safeResponse.setAddresses(newUser.getAddresses());
        safeResponse.setPassword(null);

        return ResponseEntity.ok(safeResponse);
    }

    @GetMapping //listar usuarios sortedBy
    public List<user> getUsers(@RequestParam(required = false) String sortedBy) {
        if (sortedBy == null || sortedBy.isEmpty()){
            return users;
        }
        return users.stream()
                .sorted((u1, u2) -> {
                    switch (sortedBy.toLowerCase()) {
                        case "email":
                            return u1.getEmailAddress().compareToIgnoreCase(u2.getEmailAddress());
                        case "id":
                            return u1.getId().compareTo(u2.getId());
                        case "name":
                            return u1.getName().compareToIgnoreCase(u2.getName());
                        case "phone":
                            return u1.getPhone().compareToIgnoreCase(u2.getPhone());
                        case "taxid":
                            return u1.getTaxID().compareToIgnoreCase(u2.getTaxID());
                        case "createdAt":
                            if (u1.getCreatedAt() == null || u2.getCreatedAt() == null) return 0;
                            return u1.getCreatedAt().compareTo(u2.getCreatedAt());
                        default:
                            return 0;
                    }
                })
                .toList();
    }

    @DeleteMapping("/{id}") //deletar usuario por id
    public String deleteUser(@PathVariable UUID id) {
        boolean removed = users.removeIf(user -> user.getId().equals(id));
        if (removed){
            return "User with Id " + id + "deleted successfully";
        }else{
            return "User with Id " + id + " not found";
        }
    }

    @PatchMapping("/{id}") //atualizar usuario por id
    public user update(@PathVariable UUID id, @RequestBody user updateFields) {
        for (user user : users) {
            if (user.getId().equals(id)) {
                if (updateFields.getEmailAddress() != null) user.setEmailAddress(updateFields.getEmailAddress());
                if (updateFields.getName() != null) user.setName(updateFields.getName());
                if (updateFields.getPhone() != null) user.setPhone(updateFields.getPhone());
                if (updateFields.getTaxID() != null) user.setTaxID(updateFields.getTaxID());
                if (updateFields.getAddresses() != null) user.setAddresses(updateFields.getAddresses());
                return user;
            }
        }
        return null;
    }

    @GetMapping("/filter")
    public List<user> filterUsers(@RequestParam String filter) {
        if (filter == null || filter.isEmpty()) {
            return users;
        }

        String [] parts = filter.split("\\+");
        if (parts.length != 3) {
            return users;
        }

        String field = parts[0].toLowerCase();
        String operator = parts [1];
        String value = parts [2].toLowerCase();

        return users.stream()
            .filter(user -> {
                String fieldValue = null;

                switch (field) {
                    case "emailAddress":
                        fieldValue = user.getEmailAddress();
                        break;
                    case "id":
                        fieldValue = user.getId() != null ? user.getId().toString() : null;
                        break;
                    case "name":
                        fieldValue = user.getName();
                        break;
                    case "phone":
                        fieldValue = user.getPhone();
                        break;
                    case "tax_id":
                        fieldValue = user.getTaxID();
                        break;
                    case "creadtedAt":
                        fieldValue = user.getCreatedAt() != null ? user.getCreatedAt().toString() : null;
                        break;
                }

                if (fieldValue == null) return false;
                fieldValue = fieldValue.toLowerCase();

                switch (operator) {
                    case "co":
                        return fieldValue.contains(value);
                    case "eq":
                        return fieldValue.equals(value);
                    case "sw":
                        return fieldValue.startsWith(value);
                    case "ew":
                        return fieldValue.endsWith(value);
                    default:
                        return false;
                }
            })
        .toList();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody user loginRequest) {
        if (loginRequest.getTaxID() == null || loginRequest.getPassword() == null) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "Tax ID and password are required"
            ));
        }

        for (user user : users) {
            if (user.getTaxID() != null && user.getTaxID().equalsIgnoreCase(loginRequest.getTaxID())) {
                if (user.getPassword() == null) {
                    return ResponseEntity.status(500).body(Map.of(
                            "status", "error",
                            "message", "Stored password is invalid (null)"
                    ));
                }

                String decrypted = cryptoUtils.decrypt(user.getPassword());
                if (decrypted.equals(loginRequest.getPassword())) {
                    String token = Base64.getEncoder().encodeToString(
                            (user.getTaxID() + ":" + UUID.randomUUID()).getBytes()
                    );

                    return ResponseEntity.ok(Map.of(
                            "status", "success",
                            "message", "Login successful",
                            "taxId", user.getTaxID(),
                            "token", token
                    ));
                } else {
                    return ResponseEntity.status(401).body(Map.of(
                            "status", "error",
                            "message", "Invalid password"
                    ));
                }
            }
        }

        return ResponseEntity.status(404).body(Map.of(
                "status", "error",
                "message", "User not found"
        ));
    }
}
