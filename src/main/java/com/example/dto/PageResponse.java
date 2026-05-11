package com.example.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import java.util.List;

/**
 * 分页响应DTO
 */
@Data
@AllArgsConstructor
public class PageResponse<T> {
    private List<T> records;
    private Long total;
    private Integer pageNum;
    private Integer pageSize;
    private Integer pages;
}
