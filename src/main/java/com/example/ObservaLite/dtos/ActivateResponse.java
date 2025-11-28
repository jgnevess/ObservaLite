package com.example.ObservaLite.dtos;

import lombok.Data;

@Data
public class ActivateResponse {
    private UserResponseDto userResponseDto;
    private String message;
}
