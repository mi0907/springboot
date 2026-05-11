package com.example.service;

import com.example.dto.*;
import com.example.entity.Role;
import com.example.entity.User;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户业务服务
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserDto register(LoginRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getUsername() + "@example.com");
        user.setStatus(1);

        // 默认赋予user角色
        Role userRole = roleRepository.findByCode("user")
                .orElseThrow(() -> new RuntimeException("用户角色不存在"));
        user.setRoles(new ArrayList<>(List.of(userRole)));

        User savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        return convertToDto(user);
    }

    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        return convertToDto(user);
    }

    public PageResponse<UserDto> getUserPage(Integer pageNum, Integer pageSize, String username) {
        PageHelper.startPage(pageNum, pageSize);
        List<User> users;
        if (username != null && !username.isEmpty()) {
            users = userRepository.findByUsernameContaining(username);
        } else {
            users = userRepository.findAll();
        }
        PageInfo<User> pageInfo = new PageInfo<>(users);

        List<UserDto> userDtos = users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return new PageResponse<>(userDtos, pageInfo.getTotal(), pageNum, pageSize, pageInfo.getPages());
    }

    @Transactional
    public UserDto updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        if (userDto.getRealName() != null) {
            user.setRealName(userDto.getRealName());
        }
        if (userDto.getAvatarUrl() != null) {
            user.setAvatarUrl(userDto.getAvatarUrl());
        }

        User updated = userRepository.save(user);
        return convertToDto(updated);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        user.setStatus(0);  // 逻辑删除
        userRepository.save(user);
    }

    @Transactional
    public void updateLastLogin(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
    }

    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRealName(user.getRealName());
        dto.setAvatarUrl(user.getAvatarUrl());
        dto.setStatus(user.getStatus());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());

        if (user.getRoles() != null) {
            dto.setRoles(user.getRoles().stream()
                    .map(Role::getCode)
                    .collect(Collectors.toList()));

            List<String> permissions = user.getRoles().stream()
                    .flatMap(role -> role.getPermissions().stream())
                    .map(p -> p.getCode())
                    .distinct()
                    .collect(Collectors.toList());
            dto.setPermissions(permissions);
        }

        return dto;
    }
}
