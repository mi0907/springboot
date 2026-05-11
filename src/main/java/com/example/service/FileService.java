package com.example.service;

import com.example.dto.FileUploadResponse;
import com.example.entity.FileRecord;
import com.example.repository.FileRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 文件业务服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRecordRepository fileRecordRepository;

    @Value("${file.upload-dir:./uploads/}")
    private String uploadDir;

    @Value("${file.allowed-extensions:jpg,jpeg,png,gif,pdf,doc,docx,xls,xlsx,zip}")
    private String allowedExtensions;

    @Value("${file.max-file-size:52428800}")
    private long maxFileSize;

    public FileUploadResponse uploadFile(MultipartFile file) throws IOException {
        // 验证文件
        validateFile(file);

        // 创建上传目录
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 生成新文件名
        String originalFileName = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFileName);
        String storedFileName = UUID.randomUUID() + "." + fileExtension;
        String filePath = Paths.get(uploadDir, storedFileName).toString();

        // 保存文件
        Files.write(Paths.get(filePath), file.getBytes());

        // 保存文件记录
        FileRecord record = new FileRecord();
        record.setOriginalFileName(originalFileName);
        record.setStoredFileName(storedFileName);
        record.setFilePath(filePath);
        record.setFileSize(file.getSize());
        record.setFileType(fileExtension);
        record.setUploadUserId(1L);  // 实际应该从当前用户获取
        record.setCreatedAt(LocalDateTime.now());

        FileRecord saved = fileRecordRepository.save(record);

        // 返回响应
        FileUploadResponse response = new FileUploadResponse();
        response.setId(saved.getId());
        response.setOriginalFileName(originalFileName);
        response.setStoredFileName(storedFileName);
        response.setFileUrl("/api/files/download/" + saved.getId());
        response.setFileSize(file.getSize());
        response.setFileType(fileExtension);
        response.setUploadTime(saved.getCreatedAt());

        log.info("文件上传成功: {}", originalFileName);
        return response;
    }

    public byte[] downloadFile(Long fileId) throws IOException {
        FileRecord record = fileRecordRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("文件不存在"));

        Path path = Paths.get(record.getFilePath());
        if (!Files.exists(path)) {
            throw new RuntimeException("文件已被删除");
        }

        return Files.readAllBytes(path);
    }

    public void deleteFile(Long fileId) throws IOException {
        FileRecord record = fileRecordRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("文件不存在"));

        Path path = Paths.get(record.getFilePath());
        if (Files.exists(path)) {
            Files.delete(path);
        }

        fileRecordRepository.deleteById(fileId);
        log.info("文件删除成功: {}", record.getOriginalFileName());
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("文件不能为空");
        }

        if (file.getSize() > maxFileSize) {
            throw new RuntimeException("文件大小超过限制: " + maxFileSize / (1024 * 1024) + "MB");
        }

        String fileExtension = getFileExtension(file.getOriginalFilename()).toLowerCase();
        if (!allowedExtensions.toLowerCase().contains(fileExtension)) {
            throw new RuntimeException("不支持的文件类型: " + fileExtension);
        }
    }

    private String getFileExtension(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        return lastDot > 0 ? fileName.substring(lastDot + 1) : "";
    }
}
