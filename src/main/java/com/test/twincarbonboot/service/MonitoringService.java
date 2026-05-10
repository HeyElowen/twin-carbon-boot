package com.test.twincarbonboot.service;


import com.test.twincarbonboot.pojo.CarbonCustomPoint;
import com.test.twincarbonboot.pojo.CarbonEmissionPoint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public interface MonitoringService {

    List<CarbonEmissionPoint> selectByYearAndQuarter(int year, String quarter);

    List<CarbonCustomPoint> selectCustomByYearAndQuarter(@Min(2000) @Max(2100) @NotNull(message = "年度不能为空") Integer year, @Pattern(regexp = "Q1|Q2|Q3|Q4|ALL",message = "季度只能是Q1,Q2,Q3,Q4,ALL") String quarter);

    List selectCategoryRatio(@Min(2000) @Max(2100) @NotNull(message = "年度不能为空") Integer year, @Pattern(regexp = "^(Q1|Q2|Q3|Q4|ALL)?$", message = "季度只能是 Q1/Q2/Q3/Q4/ALL") String quarter);

    List selectTrend(@Min(2000) @Max(2100) @NotNull(message = "年度不能为空") Integer yearStart, @Min(2000) @Max(2100) Integer yearEnd, @Pattern(regexp = "^(工业区|农业区|住宅区|商业区|教育区)?$", message = "用地类型只能是工业区|农业区|住宅区|商业区|教育区") String category);
}
