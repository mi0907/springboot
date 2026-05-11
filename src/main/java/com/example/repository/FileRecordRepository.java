package com.example.repository;

import com.example.entity.FileRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 文件记录数据访问层
 */
@Repository
public interface FileRecordRepository extends JpaRepository<FileRecord, Long> {
}
