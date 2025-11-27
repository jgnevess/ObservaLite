package com.example.ObservaLite.dtos;

import com.example.ObservaLite.entities.auth.User;
import lombok.Data;

import java.time.Instant;

@Data
public class UserResponseDto {
    private String firstName;
    private String lastName;
    private String validEmail;
    private Instant accountExpiresAt;

    public UserResponseDto(User user) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.validEmail = user.getValidEmail();
        this.accountExpiresAt = user.getAccountExpiresAt();
    }
}
