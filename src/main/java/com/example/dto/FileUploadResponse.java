package com.example.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 文件上传响应DTO
 */
@Data
public class FileUploadResponse {
    private Long id;
    private String originalFileName;
    private String storedFileName;
    private String fileUrl;
    private Long fileSize;
    private String fileType;
    private LocalDateTime uploadTime;
}
