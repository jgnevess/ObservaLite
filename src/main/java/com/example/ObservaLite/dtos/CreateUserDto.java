package com.example.ObservaLite.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserDto {
    private String firstName;
    private String lastName;
    @Size(max = 48, min = 8, message = "Min 8 caracteres, maximo 48 caracteres")
    private String password;
    @Email
    private String validEmail;
}
