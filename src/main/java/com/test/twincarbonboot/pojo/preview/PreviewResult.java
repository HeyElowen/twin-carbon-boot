package com.test.twincarbonboot.pojo.preview;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class PreviewResult {

    private String batchId;
    private int totalCount;
    private int validCount;
    private int invalidCount;
    private List<PreviewPoint> previewPoints;
    private List<Map<String, Object>> rawDataList;
    private List<ImportError> errors;
}
