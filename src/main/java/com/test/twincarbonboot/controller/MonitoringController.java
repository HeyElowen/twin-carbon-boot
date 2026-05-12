package com.test.twincarbonboot.controller;

import com.test.twincarbonboot.cache.PreviewCache;
import com.test.twincarbonboot.pojo.CarbonCustomPoint;
import com.test.twincarbonboot.pojo.CarbonEmissionPoint;
import com.test.twincarbonboot.pojo.Result;
import com.test.twincarbonboot.pojo.preview.PreviewResult;
import com.test.twincarbonboot.service.MonitoringService;
import com.test.twincarbonboot.service.excel.ExcelImportService;
import com.test.twincarbonboot.utils.UserContext;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@Validated
@RequestMapping("/monitoring")
public class MonitoringController {

    @Autowired
    private MonitoringService monitoringService;

    @Autowired
    private ExcelImportService excelImportService;

    @Autowired
    private PreviewCache previewCache;

    //数据点获取
    @GetMapping("/observation-point")
    public Result<List<CarbonEmissionPoint>> getObservationPoint(
            @RequestParam @Min(2000) @Max(2100) @NotNull(message = "年度不能为空") Integer year,
            @RequestParam @Pattern(regexp = "^(Q1|Q2|Q3|Q4|ALL)?$", message = "季度只能是 Q1/Q2/Q3/Q4/ALL") String quarter) {
        List<CarbonEmissionPoint> pointList = monitoringService.selectByYearAndQuarter(year, quarter);
        return Result.success(pointList);
    }

    //数据点获取-自定义
    @GetMapping("/custom-observation-point")
    public Result<List<CarbonCustomPoint>> getCustomObservationPoint(
            @RequestParam @Min(2000) @Max(2100) @NotNull(message = "年度不能为空") Integer year,
            @RequestParam @Pattern(regexp = "^(Q1|Q2|Q3|Q4|ALL)?$", message = "季度只能是 Q1/Q2/Q3/Q4/ALL") String quarter) {
        List<CarbonCustomPoint> pointList = monitoringService.selectCustomByYearAndQuarter(year, quarter);
        return Result.success(pointList);
    }

    //饼图查询-自定义
    @GetMapping("/statistics/custom-category-ratio")
    public Result<List> getCustomStatisticsCategoryRatio(
            @RequestParam @Min(2000) @Max(2100) @NotNull(message = "年度不能为空") Integer year,
            @RequestParam @Pattern(regexp = "^(Q1|Q2|Q3|Q4|ALL)?$", message = "季度只能是 Q1/Q2/Q3/Q4/ALL") String quarter) {
        List ratio = monitoringService.selectCustomCategoryRatio(year, quarter);
        return Result.success(ratio);
    }

    //饼图查询
    @GetMapping("/statistics/category-ratio")
    public Result<List> getStatisticsCategoryRatio(
            @RequestParam @Min(2000) @Max(2100) @NotNull(message = "年度不能为空") Integer year,
            @RequestParam @Pattern(regexp = "^(Q1|Q2|Q3|Q4|ALL)?$", message = "季度只能是 Q1/Q2/Q3/Q4/ALL") String quarter) {
        List ratio = monitoringService.selectCategoryRatio(year, quarter);
        return Result.success(ratio);
    }


    //折线图查询
    @GetMapping("/statistics/trend")
    public Result<List> getStatisticsTrend(
            @RequestParam @Min(2000) @Max(2100) @NotNull(message = "起始年度不能为空") Integer yearStart,
            @RequestParam(required = false) @Min(2000) @Max(2100)    Integer yearEnd,
            @RequestParam @Pattern(regexp = "^(工业区|农业区|住宅区|商业区|教育区)?$", message = "用地类型只能是工业区|农业区|住宅区|商业区|教育区")  String category

    ){
        List trend = monitoringService.selectTrend(yearStart,yearEnd,category);

        return Result.success(trend);
    }

    //折线图查询--自定义数据
    @GetMapping("/statistics/custom-trend")
    public Result<List> getCustomStatisticsTrend(
            @RequestParam @Min(2000) @Max(2100) @NotNull(message = "起始年度不能为空") Integer yearStart,
            @RequestParam(required = false) @Min(2000) @Max(2100)    Integer yearEnd,
            @RequestParam @Pattern(regexp = "^(工业区|农业区|住宅区|商业区|教育区)?$", message = "用地类型只能是工业区|农业区|住宅区|商业区|教育区")  String category

    ){
        List trend = monitoringService.selectCustomTrend(yearStart,yearEnd,category);

        return Result.success(trend);
    }


    //对象查询
    @GetMapping("/query")
    public Result<List<CarbonCustomPoint>> getQueryPoint(
            @RequestParam String name,
            @RequestParam @Min(2000) @Max(2100) @NotNull(message = "年度不能为空") Integer year,
            @RequestParam @Pattern(regexp = "Q1|Q2|Q3|Q4|ALL", message = "季度只能是 Q1/Q2/Q3/Q4/ALL") String quarter){

        List query = monitoringService.selectByName(name,year,quarter);

        return Result.success(query);

    }


    //excel模板下载
    @GetMapping("/template/download")
    public ResponseEntity<ClassPathResource> templateDownload(){

        ClassPathResource resource = new ClassPathResource("templates/carbon_import_template.xlsx");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"carbon_import_template.xlsx\"")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(resource);

    }
    //excel上传
    @PostMapping("/import")
    public Result<PreviewResult> importExcel(@RequestParam("file") MultipartFile file) throws IOException {
        PreviewResult previewResult = excelImportService.parse(file);
        previewCache.put(previewResult.getBatchId(), previewResult,UserContext.getUserId());
        log.info("Excel导入预览: batchId={}, total={}, valid={}, invalid={}",
                previewResult.getBatchId(), previewResult.getTotalCount(),
                previewResult.getValidCount(), previewResult.getInvalidCount());
        return Result.success(previewResult);
    }

    //确认上传
    @PostMapping("/import/confirm")
    public Result<Void> confirmImport(@RequestBody Map<String, String> body) {
        String batchId = body.get("batchId");
        if (batchId == null || batchId.isEmpty()) {
            return Result.error("batchId不能为空");
        }
        monitoringService.confirmImport(batchId);
        return Result.success();
    }


}
