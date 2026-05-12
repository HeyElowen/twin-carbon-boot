package com.test.twincarbonboot.pojo.preview;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ImportError {
    private int row;       // Excel 行号（从 1 开始）
    private String field;  // 字段名
    private String message; // 错误原因
    private List<Map<String, Object>> rawDataList;
}

