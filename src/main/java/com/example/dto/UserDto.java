package com.example.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户数据传输对象
 */
@Data
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String realName;
    private String avatarUrl;
    private Integer status;
    private List<String> roles;
    private List<String> permissions;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
