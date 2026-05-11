package com.example.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * 文件记录实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "file_record")
public class FileRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String originalFileName;

    @Column(nullable = false, length = 500)
    private String storedFileName;

    @Column(nullable = false, length = 500)
    private String filePath;

    @Column(nullable = false)
    private Long fileSize;

    @Column(nullable = false, length = 50)
    private String fileType;

    @Column(name = "upload_user_id")
    private Long uploadUserId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
