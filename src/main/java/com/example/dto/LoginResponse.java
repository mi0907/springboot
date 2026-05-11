package com.example.dto;

import lombok.Data;
import lombok.AllArgsConstructor;

/**
 * 登录响应DTO
 */
@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String refreshToken;
    private Long expiresIn;
    private UserDto user;
}
