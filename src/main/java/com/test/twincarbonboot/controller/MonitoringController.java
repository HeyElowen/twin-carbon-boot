package com.test.twincarbonboot.controller;

import com.test.twincarbonboot.pojo.CarbonCustomPoint;
import com.test.twincarbonboot.pojo.CarbonEmissionPoint;
import com.test.twincarbonboot.pojo.Result;
import com.test.twincarbonboot.service.MonitoringService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping("/monitoring")
public class MonitoringController {

    @Autowired
    private MonitoringService monitoringService;

    @GetMapping("/observation-point")
    public Result<List<CarbonEmissionPoint>> getObservationPoint(
            @RequestParam @Min(2000) @Max(2100) @NotNull(message = "年度不能为空") Integer year,
            @RequestParam @Pattern(regexp = "^(Q1|Q2|Q3|Q4|ALL)?$", message = "季度只能是 Q1/Q2/Q3/Q4/ALL") String quarter) {
        List<CarbonEmissionPoint> pointList = monitoringService.selectByYearAndQuarter(year, quarter);
        return Result.success(pointList);
    }

    @GetMapping("/custom-observation-point")
    public Result<List<CarbonCustomPoint>> getCustomObservationPoint(
            @RequestParam @Min(2000) @Max(2100) @NotNull(message = "年度不能为空") Integer year,
            @RequestParam @Pattern(regexp = "^(Q1|Q2|Q3|Q4|ALL)?$", message = "季度只能是 Q1/Q2/Q3/Q4/ALL") String quarter) {
        List<CarbonCustomPoint> pointList = monitoringService.selectCustomByYearAndQuarter(year, quarter);
        return Result.success(pointList);
    }

    @GetMapping("/statistics/category-ratio")
    public Result<List> getStatisticsCategoryRatio(
            @RequestParam @Min(2000) @Max(2100) @NotNull(message = "年度不能为空") Integer year,
            @RequestParam @Pattern(regexp = "^(Q1|Q2|Q3|Q4|ALL)?$", message = "季度只能是 Q1/Q2/Q3/Q4/ALL") String quarter) {
        List ratio = monitoringService.selectCategoryRatio(year, quarter);
        return Result.success(ratio);
    }

    @GetMapping("/statistics/trend")
    public Result<List> getStatisticsTrend(
            @RequestParam @Min(2000) @Max(2100) @NotNull(message = "起始年度不能为空") Integer yearStart,
            @RequestParam(required = false) @Min(2000) @Max(2100)    Integer yearEnd,
            @RequestParam @Pattern(regexp = "^(工业区|农业区|住宅区|商业区|教育区)?$", message = "用地类型只能是工业区|农业区|住宅区|商业区|教育区")  String category

    ){
        List trend = monitoringService.selectTrend(yearStart,yearEnd,category);

        return Result.success(trend);
    }

}
