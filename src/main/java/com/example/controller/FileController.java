package com.example.controller;

import com.example.common.response.R;
import com.example.dto.FileUploadResponse;
import com.example.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 文件管理控制器
 */
@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
@Tag(name = "文件管理", description = "文件上传、下载、删除等操作")
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    @Operation(summary = "上传文件")
    @PreAuthorize("hasAuthority('file:upload')")
    public R<FileUploadResponse> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        FileUploadResponse response = fileService.uploadFile(file);
        return R.success("文件上传成功", response);
    }

    @GetMapping("/download/{fileId}")
    @Operation(summary = "下载文件")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long fileId) throws IOException {
        byte[] fileContent = fileService.downloadFile(fileId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(fileContent);
    }

    @DeleteMapping("/{fileId}")
    @Operation(summary = "删除文件")
    @PreAuthorize("hasAuthority('file:delete')")
    public R<Void> deleteFile(@PathVariable Long fileId) throws IOException {
        fileService.deleteFile(fileId);
        return R.success("文件删除成功", null);
    }
}
