package com.example.controller;

import com.example.common.response.R;
import com.example.dto.PageResponse;
import com.example.dto.UserDto;
import com.example.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理控制器
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "用户管理", description = "用户信息相关接口")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    @Operation(summary = "获取用户详情")
    public R<UserDto> getUser(@PathVariable Long id) {
        return R.success(userService.getUserById(id));
    }

    @GetMapping
    @Operation(summary = "分页获取用户列表")
    @PreAuthorize("hasAuthority('user:read')")
    public R<PageResponse<UserDto>> getUserPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String username) {
        PageResponse<UserDto> page = userService.getUserPage(pageNum, pageSize, username);
        return R.success(page);
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改用户信息")
    @PreAuthorize("hasAuthority('user:update')")
    public R<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        return R.success(userService.updateUser(id, userDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户")
    @PreAuthorize("hasAuthority('user:delete')")
    public R<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return R.success(null);
    }
}
