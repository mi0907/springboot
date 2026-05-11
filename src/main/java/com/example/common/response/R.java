package com.example.common.response;

import lombok.Data;
import java.io.Serializable;

/**
 * 统一API响应类
 */
@Data
public class R<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer code;
    private String message;
    private T data;

    public R() {}

    public R(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public R(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> R<T> success(T data) {
        return new R<>(0, "success", data);
    }

    public static <T> R<T> success(String message, T data) {
        return new R<>(0, message, data);
    }

    public static <T> R<T> fail(Integer code, String message) {
        return new R<>(code, message);
    }

    public static <T> R<T> fail(String message) {
        return new R<>(1, message);
    }
}
