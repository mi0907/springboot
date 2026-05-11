package com.example.controller;

import com.example.common.response.R;
import com.example.dto.LoginRequest;
import com.example.dto.LoginResponse;
import com.example.dto.UserDto;
import com.example.security.JwtTokenProvider;
import com.example.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * 认证管理控制器
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "认证管理", description = "用户登录、注册等认证相关接口")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserService userService;

    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public R<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        String token = tokenProvider.generateToken(authentication);
        String refreshToken = tokenProvider.generateRefreshToken(request.getUsername());

        userService.updateLastLogin(request.getUsername());
        UserDto userDto = userService.getUserByUsername(request.getUsername());

        LoginResponse response = new LoginResponse(
                token,
                refreshToken,
                86400000L,  // 24小时
                userDto
        );

        return R.success("登录成功", response);
    }

    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public R<UserDto> register(@Valid @RequestBody LoginRequest request) {
        UserDto user = userService.register(request);
        return R.success("注册成功", user);
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "刷新令牌")
    public R<LoginResponse> refreshToken(@RequestHeader("Authorization") String refreshToken) {
        if (!refreshToken.startsWith("Bearer ")) {
            return R.fail("无效的令牌格式");
        }

        String token = refreshToken.substring(7);
        try {
            tokenProvider.validateToken(token);
            String username = tokenProvider.getUsernameFromJWT(token);
            String newToken = tokenProvider.generateToken(username);

            UserDto userDto = userService.getUserByUsername(username);
            LoginResponse response = new LoginResponse(
                    newToken,
                    token,
                    86400000L,
                    userDto
            );
            return R.success("令牌刷新成功", response);
        } catch (Exception e) {
            return R.fail("令牌无效或已过期");
        }
    }
}
