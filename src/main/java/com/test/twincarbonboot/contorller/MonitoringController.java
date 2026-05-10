package com.test.twincarbonboot.contorller;

import com.test.twincarbonboot.pojo.CarbonCustomPoint;
import com.test.twincarbonboot.pojo.CarbonEmissionPoint;
import com.test.twincarbonboot.pojo.Result;
import com.test.twincarbonboot.service.MonitoringService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
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

    @GetMapping("/ObservationPoint")
    public Result<List<CarbonEmissionPoint>> getObservationPoint(
            @RequestParam @Min(2000)@Max(2100) @NotNull(message = "年度不能为空") Integer year,
            @RequestParam @Pattern(regexp = "^(Q1|Q2|Q3|Q4|ALL)?$", message = "季度只能是 Q1/Q2/Q3/Q4/ALL")String quarter) {

        //纪录请求参数
        log.info("【观测点查询】接收请求: year={}, quarter={}", year, quarter);

        long start = System.currentTimeMillis();
        List<CarbonEmissionPoint> pointList=monitoringService.selectByYearAndQuarter(year,quarter);


        // 记录结果和耗时
        log.info("【观测点查询】返回 {} 条数据, 耗时 {} ms",
                pointList.size(),
                System.currentTimeMillis() - start);
        return  Result.success(pointList);
    }

    @GetMapping("/CustomObservationPoint")
public Result<List<CarbonCustomPoint>> getCustomObservationPoint(
            @RequestParam @Min(2000)@Max(2100) @NotNull(message = "年度不能为空") Integer year,
            @RequestParam @Pattern(regexp = "^(Q1|Q2|Q3|Q4|ALL)?$", message = "季度只能是 Q1/Q2/Q3/Q4/ALL") String quarter
    ){
        //纪录请求参数
        log.info("【观测点查询】接收请求: year={}, quarter={}", year, quarter);

        long start = System.currentTimeMillis();
        List<CarbonCustomPoint> pointList=monitoringService.CustomOselectByYearAndQuarter(year,quarter);


        // 记录结果和耗时
        log.info("【观测点查询】返回 {} 条数据, 耗时 {} ms",
                pointList.size(),
                System.currentTimeMillis() - start);
        return  Result.success(pointList);

    }

}
