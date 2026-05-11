package com.example.common.exception;

import com.example.common.response.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public R<Void> handleAuthenticationException(AuthenticationException e) {
        log.error("认证异常:", e);
        if (e instanceof BadCredentialsException) {
            return R.fail(401, "用户名或密码错误");
        } else if (e instanceof DisabledException) {
            return R.fail(401, "用户已被禁用");
        } else if (e instanceof LockedException) {
            return R.fail(401, "用户已被锁定");
        }
        return R.fail(401, "认证失败");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public R<Void> handleAccessDeniedException(AccessDeniedException e) {
        log.error("权限异常:", e);
        return R.fail(403, "权限不足");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<Void> handleValidationException(MethodArgumentNotValidException e) {
        log.error("参数验证异常:", e);
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return R.fail(400, message);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public R<Void> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        log.error("文件过大异常:", e);
        return R.fail(400, "上传文件过大");
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public R<Void> handleNoHandlerFoundException(NoHandlerFoundException e) {
        log.error("404异常:", e);
        return R.fail(404, "请求的资源不存在");
    }

    @ExceptionHandler(RuntimeException.class)
    public R<Void> handleRuntimeException(RuntimeException e) {
        log.error("运行时异常:", e);
        return R.fail(500, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public R<Void> handleException(Exception e) {
        log.error("系统异常:", e);
        return R.fail(500, "系统内部错误");
    }
}
